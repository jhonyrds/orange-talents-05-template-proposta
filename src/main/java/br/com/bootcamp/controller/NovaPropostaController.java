package br.com.bootcamp.controller;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.repository.AnaliseSolicitacaoClient;
import br.com.bootcamp.repository.NovaPropostaRepository;
import br.com.bootcamp.dto.request.NovaPropostaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private AnaliseSolicitacaoClient analiseSolicitacaoClient;

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request,
                                       UriComponentsBuilder builder) throws JsonMappingException, JsonProcessingException {

        String existeProposta = repository.findByDocumento(request.getDocumento());

        if (existeProposta == null) {
            NovaProposta proposta = repository.save(request.toModel());
            proposta.realizarAnalise(analiseSolicitacaoClient);
            repository.save(proposta);
            URI urlRetorno = builder.path("/proposta/{id}")
                    .buildAndExpand(proposta.getIdProposta()).toUri();

            return ResponseEntity.created(urlRetorno).build();
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Já existe proposta para esse documento: " + request.getDocumento());
    }
}