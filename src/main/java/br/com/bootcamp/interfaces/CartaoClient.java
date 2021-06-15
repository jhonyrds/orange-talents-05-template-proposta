package br.com.bootcamp.interfaces;

import br.com.bootcamp.dto.request.AvisoViagemRequest;
import br.com.bootcamp.dto.request.BloqueioCartaoRequest;
import br.com.bootcamp.dto.request.CartaoRequest;
import br.com.bootcamp.dto.request.CarteiraRequest;
import br.com.bootcamp.dto.response.AvisoViagemResponse;
import br.com.bootcamp.dto.response.BloqueioCartaoResponse;
import br.com.bootcamp.dto.response.CartaoResponse;
import br.com.bootcamp.dto.response.CarteiraResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartao", url = "${analise.cartao}")
public interface CartaoClient {

    @PostMapping
    CartaoResponse cadastraPropostaCartao(CartaoRequest request);

    @GetMapping
    CartaoResponse buscaCartaoProposta(@RequestParam(value = "id") String id);

    @PostMapping("/{idCartao}/bloqueio")
    BloqueioCartaoResponse bloquearCartao(@PathVariable("idCartao") String idCartao, BloqueioCartaoRequest request);

    @PostMapping("/{idCartao}/aviso")
    AvisoViagemResponse avisarSobreViagem(@PathVariable("idCartao") String idCartao, AvisoViagemRequest aviso);

    @PostMapping("/{idCartao}/carteira")
    CarteiraResponse adicionarCarteira(@PathVariable("idCartao") String idCartao, CarteiraRequest carteira);
}
