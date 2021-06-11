package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.BiometriaRequest;
import br.com.bootcamp.model.Biometria;
import br.com.bootcamp.model.Cartao;
import br.com.bootcamp.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartao")
public class CartaoController {

    private CartaoRepository cartaoRepository;

    public CartaoController(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/biometrias")
    public ResponseEntity<?> cadastrarBiometria(@PathParam(value = "uuidCartao") String uuidCartao,
                                                @RequestBody @Valid BiometriaRequest request, UriComponentsBuilder uriBuilder){

        Optional<Cartao> cartao = cartaoRepository.findByUuid(uuidCartao);

        if (cartao.isPresent()){
            Biometria biometria = new Biometria(request.getImpressaoDigital());
            cartao.get().cadastrarBiometria(biometria);
            cartaoRepository.save(cartao.get());

            URI uri = uriBuilder.path("biometria/{uuid}").build(biometria.getUuid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }
}
