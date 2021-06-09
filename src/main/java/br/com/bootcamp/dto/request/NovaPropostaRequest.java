package br.com.bootcamp.dto.request;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.util.CPFOrCNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NovaPropostaRequest {
    @CPFOrCNPJ
    @NotBlank
    private String documento;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @Positive
    @NotNull
    private BigDecimal salarioBruto;

    public NovaPropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salarioBruto) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salarioBruto = salarioBruto;
    }

    public String getDocumento() {
        return documento;
    }

    @Override
    public String toString() {
        return "NovaPropostaRequest{" +
                "documento='" + documento + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", salarioBruto=" + salarioBruto +
                '}';
    }

    public NovaProposta toModel() {
        return new NovaProposta(documento, email, nome, endereco, salarioBruto);
    }
}
