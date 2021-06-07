package br.com.bootcamp.repository;

import br.com.bootcamp.dto.response.PropostaResponse;
import br.com.bootcamp.dto.request.AnaliseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "analise-de-propostas", url = "${analise.proposta}")
public interface AnaliseSolicitacaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    PropostaResponse analisarProposta(AnaliseRequest request);


}
