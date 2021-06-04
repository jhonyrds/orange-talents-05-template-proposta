package br.com.bootcamp.controller;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.request.NovaPropostaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposta")
public class NovaPropostaController {
    @PersistenceContext
    private EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request, UriComponentsBuilder uriComponentsBuilder){
        NovaProposta proposta = request.toModel(manager);
        manager.persist(proposta);

        URI urlRetorno = uriComponentsBuilder.path("/retorno-proposta/{id}")
                .buildAndExpand(proposta.getId()).toUri();

        return ResponseEntity.created(urlRetorno).build();
    }
}
