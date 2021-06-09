package br.com.bootcamp.dto.response;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;

public class PropostaResponse {

    private Long idProposta;
    private String documento;
    private String nome;
    private StatusProposta statusProposta;

    public PropostaResponse(NovaProposta proposta) {
        this.idProposta = proposta.getId();
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.statusProposta = proposta.getStatusProposta();
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

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }
}
