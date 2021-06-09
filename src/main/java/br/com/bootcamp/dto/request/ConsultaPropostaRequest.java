package br.com.bootcamp.dto.request;

import br.com.bootcamp.util.CPFOrCNPJ;

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

    @Override
    public String toString() {
        return "ConsultaPropostaRequest{" +
                "documento='" + documento + '\'' +
                ", nome='" + nome + '\'' +
                ", idProposta='" + idProposta + '\'' +
                '}';
    }
}
