package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.AnaliseRequest;
import br.com.bootcamp.dto.request.NovaPropostaRequest;
import br.com.bootcamp.dto.response.AnalisePropostaResponse;
import br.com.bootcamp.dto.response.PropostaResponse;
import br.com.bootcamp.interfaces.AnaliseSolicitacaoClient;
import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.repository.NovaPropostaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/proposta")
public class NovaPropostaController {

    private NovaPropostaRepository propostaRepository;

    private AnaliseSolicitacaoClient analiseSolicitacao;

    private Tracer tracer;

    public NovaPropostaController(NovaPropostaRepository propostaRepository, AnaliseSolicitacaoClient analiseSolicitacao, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.analiseSolicitacao = analiseSolicitacao;
        this.tracer = tracer;
    }

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request,
                                       UriComponentsBuilder builder) throws JsonMappingException, JsonProcessingException {

        Span activeSpan = tracer.activeSpan();
        String userEmail = activeSpan.getBaggageItem("user.email");
        activeSpan.setBaggageItem("user.email", userEmail);

        if (propostaRepository.existsByDocumento(request.getDocumento())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma proposta com este documento: " + request.getDocumento());
        }

        NovaProposta proposta = request.toModel();
        propostaRepository.save(proposta);
        realizarAnalise(proposta);
        propostaRepository.save(proposta);

        URI uriLocation = builder.path("propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uriLocation).build();

    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> consultaProposta(@PathVariable("id") Long id) {

        Span activeSpan = tracer.activeSpan();
        String userEmail = activeSpan.getBaggageItem("user.email");
        activeSpan.setBaggageItem("user.email", userEmail);

        Optional<NovaProposta> proposta = propostaRepository.findById(id);
        if (proposta.isPresent()) {
            return ResponseEntity.ok().body(new PropostaResponse(proposta.get()));
        }
        return ResponseEntity.notFound().build();
    }

    private void realizarAnalise(NovaProposta proposta) throws JsonProcessingException {
        try {
            AnaliseRequest analiseRequest = new AnaliseRequest(proposta);
            AnalisePropostaResponse analiseResponse = analiseSolicitacao.analisarProposta(analiseRequest);
            if (analiseResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
                proposta.setStatusProposta(StatusProposta.ELEGIVEL);
            }
        } catch (FeignException e) {
            proposta.setStatusProposta(StatusProposta.NAO_ELEGIVEL);
        }
    }
}
