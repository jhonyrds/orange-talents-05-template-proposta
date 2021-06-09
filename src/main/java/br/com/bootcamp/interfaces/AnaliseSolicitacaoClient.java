package br.com.bootcamp.interfaces;

import br.com.bootcamp.dto.request.AnaliseRequest;
import br.com.bootcamp.dto.response.AnalisePropostaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "analise-de-propostas", url = "${analise.proposta}")
public interface AnaliseSolicitacaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "${analise.requested}")
    AnalisePropostaResponse analisarProposta(AnaliseRequest request);


}
