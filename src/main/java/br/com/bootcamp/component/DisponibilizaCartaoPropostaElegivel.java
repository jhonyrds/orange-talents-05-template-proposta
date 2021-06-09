package br.com.bootcamp.component;

import br.com.bootcamp.dto.request.CartaoRequest;
import br.com.bootcamp.dto.response.CartaoResponse;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.repository.NovaPropostaRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DisponibilizaCartaoPropostaElegivel {

    @Autowired
    private NovaPropostaRepository repository;

    @Autowired
    CartaoClient cartaoClient;

    @Scheduled(initialDelay = 6000, fixedRate = 6000)
    public void disponibilizarCartao() {

        List<NovaProposta> propostas = repository.findByStatusPropostaEqualsAndIdCartaoIsNull(StatusProposta.ELEGIVEL);

        for (NovaProposta proposta : propostas) {
            try {
                CartaoResponse propostaCadastroProvedoraCartao = cartaoClient
                        .buscaCartaoProposta(proposta.getId().toString());
                proposta.setIdCartao(propostaCadastroProvedoraCartao.getId());
                repository.save(proposta);
            } catch (FeignException propostaNaoLocalizada) {
                CartaoRequest cartaoRequest = new CartaoRequest(
                        proposta.getDocumento(), proposta.getNome(), proposta.getId().toString());
                try {
                    cartaoClient.cadastraPropostaCartao(cartaoRequest);
                } catch (FeignException erroNaoFoiPossivelCadastrarProposta) {
                }

            }
        }
    }
}




