package br.com.bootcamp.model;

import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.util.CPFOrCNPJ;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class NovaProposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProposta;

    @CPFOrCNPJ
    @NotBlank
    @Column(unique = true)
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

    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta;

    @Deprecated
    public NovaProposta() {
    }

    public NovaProposta(String documento, String email, String nome, String endereco, BigDecimal salarioBruto) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salarioBruto = salarioBruto;
    }

    public Long getIdProposta() {
        return idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public void setStatusProposta(StatusProposta statusProposta) {
        this.statusProposta = statusProposta;
    }
}
