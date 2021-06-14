package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.AvisoViagemRequest;
import br.com.bootcamp.dto.request.BiometriaRequest;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.AvisoViagem;
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

    @PostMapping("/biometrias/{idCartao}")
    public ResponseEntity<?> cadastrarBiometria(@PathVariable(value = "idCartao") String idCartao,
                                                @RequestBody @Valid BiometriaRequest request, UriComponentsBuilder uriBuilder){

        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()){
            Biometria biometria = new Biometria(request.getImpressaoDigital());
            cartao.get().cadastrarBiometria(biometria);
            cartaoRepository.save(cartao.get());

            URI uri = uriBuilder.path("biometria/{uuid}").build(biometria.getUuid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/bloqueio/{idCartao}")
    public ResponseEntity<?> bloquearCartao(@PathVariable(value = "idCartao") String idCartao, HttpServletRequest request){
        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

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

    @PostMapping("aviso/{id}")
    public ResponseEntity<?> avisoViagem(@PathVariable(value = "idCartao") String idCartao,
         @RequestBody @Valid AvisoViagemRequest avisoRequest, HttpServletRequest request) {

        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()) {
            String ipCliente = request.getLocalAddr();
            String userAgent = request.getHeader("User-Agent");
            AvisoViagem avisoViagem = new AvisoViagem(avisoRequest.getDestinoViagem(),
                    avisoRequest.getDataTerminoViagem(), ipCliente, userAgent, cartao.get());
            cartao.get().cadastrarAvisoViagem(avisoViagem);
            cartaoRepository.save(cartao.get());

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();

    }
}
