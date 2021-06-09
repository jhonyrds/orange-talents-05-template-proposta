package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.AnaliseRequest;
import br.com.bootcamp.dto.response.PropostaResponse;
import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.repository.AnaliseSolicitacaoClient;
import br.com.bootcamp.repository.NovaPropostaRepository;
import br.com.bootcamp.dto.request.NovaPropostaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposta")
public class NovaPropostaController {

    @Autowired
    private NovaPropostaRepository repository;

    @Autowired
    private AnaliseSolicitacaoClient analiseSolicitacao;

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request,
                                       UriComponentsBuilder builder) throws JsonMappingException, JsonProcessingException {
        if (repository.existsByDocumento(request.getDocumento())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma proposta com este documento: " + request.getDocumento());
        }

        NovaProposta proposta = request.toModel();
        repository.save(proposta);

        //atualização do status com o retorno do método
        realizarAnalise(proposta);
        repository.save(proposta);

        URI uriLocation = builder.path("propostas/{id}").build(proposta.getIdProposta());
        return ResponseEntity.created(uriLocation).build();

    }

    private void realizarAnalise(NovaProposta proposta)
            throws JsonProcessingException {
        try {
            analiseSolicitacao.analisarProposta(new AnaliseRequest(proposta));
            proposta.setStatusProposta(StatusProposta.ELEGIVEL);
        } catch (FeignException e) {
            PropostaResponse propostaResponse = new ObjectMapper().readValue(e.contentUTF8(),
                    PropostaResponse.class);
            if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()
                    && propostaResponse.getResultadoSolicitacao().equals("COM_RESTRICAO")) {
                proposta.setStatusProposta(StatusProposta.NAO_ELEGIVEL);
            }
        }
    }
}