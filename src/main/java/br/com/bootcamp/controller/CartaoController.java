package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.BiometriaRequest;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.Biometria;
import br.com.bootcamp.model.Bloqueio;
import br.com.bootcamp.model.Cartao;
import br.com.bootcamp.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartao")
public class CartaoController {

    private CartaoRepository cartaoRepository;

    private CartaoClient cartaoClient;

    public CartaoController(CartaoRepository cartaoRepository, CartaoClient cartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
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

    @PostMapping("/bloqueio")
    public ResponseEntity<?> bloquearCartao(@RequestParam(value = "uuidCartao") String uuidCartao, HttpServletRequest request){
        Optional<Cartao> cartao = cartaoRepository.findByUuid(uuidCartao);

        if (cartao.isPresent()){
            HttpStatus bloqueio = cartao.get().realizarBloqueioClient(cartaoClient);
            if (bloqueio.value() == HttpStatus.OK.value()){
                String ipClient = request.getLocalAddr();
                String userAgentClient = request.getHeader("User-Agent");
                cartao.get().adicionaDadosDeBloqueio(new Bloqueio(ipClient, userAgentClient));
                cartaoRepository.save(cartao.get());
            }
            return ResponseEntity.status(bloqueio).build();
        }
        return  ResponseEntity.notFound().build();
    }
}
