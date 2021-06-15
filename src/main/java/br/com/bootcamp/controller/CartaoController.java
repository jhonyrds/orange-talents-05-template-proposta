package br.com.bootcamp.controller;

import br.com.bootcamp.dto.request.AvisoViagemRequest;
import br.com.bootcamp.dto.request.BiometriaRequest;
import br.com.bootcamp.dto.request.CarteiraRequest;
import br.com.bootcamp.dto.response.CarteiraResponse;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.*;
import br.com.bootcamp.repository.CartaoRepository;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
                                                @RequestBody @Valid BiometriaRequest request, UriComponentsBuilder uriBuilder) {

        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()) {
            Biometria biometria = new Biometria(request.getImpressaoDigital());
            cartao.get().cadastrarBiometria(biometria);
            cartaoRepository.save(cartao.get());

            URI uri = uriBuilder.path("biometria/{uuid}").build(biometria.getUuid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/bloqueio/{idCartao}")
    public ResponseEntity<?> bloquearCartao(@PathVariable(value = "idCartao") String idCartao, HttpServletRequest request) {
        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()) {
            HttpStatus bloqueio = cartao.get().realizarBloqueioClient(cartaoClient);
            if (bloqueio.value() == HttpStatus.OK.value()) {
                String ipClient = request.getLocalAddr();
                String userAgentClient = request.getHeader("User-Agent");
                cartao.get().adicionaDadosDeBloqueio(new Bloqueio(ipClient, userAgentClient));
                cartaoRepository.save(cartao.get());
            }
            return ResponseEntity.status(bloqueio).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("aviso/{id}")
    public ResponseEntity<?> avisoViagem(@PathVariable(value = "idCartao") String idCartao,
                                         @RequestBody @Valid AvisoViagemRequest avisoRequest, HttpServletRequest request) {

        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()) {
            try {
                cartaoClient.avisarSobreViagem(idCartao, avisoRequest);
                cartao.get().cadastrarAvisoViagem(new AvisoViagem(
                        avisoRequest.getDestinoViagem(), avisoRequest.getDataTerminoViagem(),
                        request.getLocalAddr(), request.getHeader("User-Agent"), cartao.get()));
                cartaoRepository.save(cartao.get());

                return ResponseEntity.ok().build();
            } catch (FeignException e) {
                if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value())
                    return ResponseEntity.unprocessableEntity().build();
                return ResponseEntity.status(e.status()).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("{id}/carteira")
    public ResponseEntity<?> adicionaCarteira(@PathVariable(value = "idCartao") String idCartao,
                                              @RequestBody @Valid CarteiraRequest request, UriComponentsBuilder uriBuilder) {
        Optional<Cartao> cartao = cartaoRepository.findByidCartao(idCartao);

        if (cartao.isPresent()) {
            try {
                request.setCarteira("PAYPAL");
                CarteiraResponse carteiraResponse = cartaoClient.adicionarCarteira(idCartao, request);
                cartao.get().adicionarCarteiraDigital(new CarteiraDigital(carteiraResponse.getId(),
                        request.getEmail(), request.getTipoCarteira(), cartao.get()));
                cartaoRepository.save(cartao.get());

                URI uri = uriBuilder.path("/{idCartao}/carteira/{id}").build(cartao.get().getIdCartao(),
                        carteiraResponse.getId());

                return ResponseEntity.created(uri).build();

            } catch (FeignException feignException) {
                return ResponseEntity.status(feignException.status()).build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
