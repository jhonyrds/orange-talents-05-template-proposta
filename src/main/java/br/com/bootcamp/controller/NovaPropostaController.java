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
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
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

    public NovaPropostaController(NovaPropostaRepository propostaRepository, AnaliseSolicitacaoClient analiseSolicitacao) {
        this.propostaRepository = propostaRepository;
        this.analiseSolicitacao = analiseSolicitacao;
    }

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request,
                                       UriComponentsBuilder builder) throws JsonMappingException, JsonProcessingException {
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
        Optional<NovaProposta> proposta = propostaRepository.findById(id);
        if (proposta.isPresent()) {
            return ResponseEntity.ok().body(new PropostaResponse(proposta.get()));
        }
        return ResponseEntity.notFound().build();
    }

    private void realizarAnalise(NovaProposta proposta) throws JsonProcessingException {
        try {
            analiseSolicitacao.analisarProposta(new AnaliseRequest(proposta));
            proposta.setStatusProposta(StatusProposta.ELEGIVEL);
        } catch (FeignException e) {
            String corpoResposta = e.contentUTF8();
            if (!e.contentUTF8().isEmpty()) {
                AnalisePropostaResponse analisePropostaResponse = new ObjectMapper().readValue(corpoResposta,
                        AnalisePropostaResponse.class);
                if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()
                        && analisePropostaResponse.getResultadoSolicitacao().equals("COM_RESTRICAO")) {
                    proposta.setStatusProposta(StatusProposta.NAO_ELEGIVEL);
                }
            }
        }
    }
}