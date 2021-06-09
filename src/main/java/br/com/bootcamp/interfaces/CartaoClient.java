package br.com.bootcamp.interfaces;

import br.com.bootcamp.dto.request.CartaoRequest;
import br.com.bootcamp.dto.response.CartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartao", url = "${analise.cartao}")
public interface CartaoClient {

    @PostMapping
    CartaoResponse cadastraPropostaCartao(CartaoRequest request);

    @GetMapping
    CartaoResponse buscaCartaoProposta(@RequestParam(value = "id") String id);
}
