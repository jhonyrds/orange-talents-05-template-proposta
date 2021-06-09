package br.com.bootcamp.dto.request;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.util.CPFOrCNPJ;
import br.com.bootcamp.util.ExisteId;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;

public class AnaliseRequest {

    @NotBlank
    @CPFOrCNPJ
    private String documento;

    @NotBlank
    private String nome;

    @NotNull
    @ExisteId(entidade = NovaProposta.class, atributo = "idProposta")
    private Long idProposta;

    public AnaliseRequest(NovaProposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getIdProposta();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}

