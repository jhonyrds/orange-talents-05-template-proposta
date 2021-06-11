package br.com.bootcamp.component;

import br.com.bootcamp.dto.request.CartaoRequest;
import br.com.bootcamp.dto.response.CartaoResponse;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.Cartao;
import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.repository.CartaoRepository;
import br.com.bootcamp.repository.NovaPropostaRepository;
import feign.FeignException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DisponibilizaCartaoPropostaElegivel {

    private NovaPropostaRepository propostaRepository;

    private CartaoRepository cartaoRepository;

    private CartaoClient cartaoClient;

    public DisponibilizaCartaoPropostaElegivel(NovaPropostaRepository propostaRepository, CartaoRepository cartaoRepository, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(initialDelay = 6000, fixedRate = 6000)
    public void disponibilizarCartao() {

        List<NovaProposta> propostas = propostaRepository.findByStatusPropostaEqualsAndCartaoIsNull(StatusProposta.ELEGIVEL);

        for (NovaProposta proposta : propostas) {
            try {
                CartaoResponse propostaCadastroProvedoraCartao = cartaoClient
                        .buscaCartaoProposta(proposta.getId().toString());
                Cartao cartao = cartaoRepository.save(new Cartao(propostaCadastroProvedoraCartao.getId()));
                proposta.setCartao(cartao);
                propostaRepository.save(proposta);
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




