package br.com.bootcamp.controller;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.repository.NovaPropostaRepository;
import br.com.bootcamp.request.NovaPropostaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposta")
public class NovaPropostaController {

    @Autowired
    private NovaPropostaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity novaProposta(@RequestBody @Valid NovaPropostaRequest request, UriComponentsBuilder uriComponentsBuilder) {
        String existeProposta = repository.findByDocumento(request.getDocumento());

        NovaProposta proposta = request.toModel(repository);

        if (existeProposta == null) {
            repository.save(proposta);
            URI urlRetorno = uriComponentsBuilder.path("/retorno-proposta/{id}")
                    .buildAndExpand(proposta.getId()).toUri();

            return ResponseEntity.created(urlRetorno).build();
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

    }
}

