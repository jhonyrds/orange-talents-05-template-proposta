package br.com.bootcamp.dto.request;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.util.CPFOrCNPJ;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

public class ConsultaPropostaRequest {
    @NotBlank
    @CPFOrCNPJ
    private String documento;
    @NotBlank
    private String nome;
    @NotBlank
    private String idProposta;

    private String statusProposta;

    public ConsultaPropostaRequest(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    RestTemplate restTemplate = new RestTemplate();
    NovaProposta proposta = restTemplate.getForObject("http://localhost:9999/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/", NovaProposta.class);


    @Override
    public String toString() {
        return "ConsultaPropostaRequest{" +
                "documento='" + documento + '\'' +
                ", nome='" + nome + '\'' +
                ", idProposta='" + idProposta + '\'' +
                ", Proposta='" + proposta + '\'' +
                '}';
    }
}
