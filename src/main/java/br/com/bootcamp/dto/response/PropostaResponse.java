package br.com.bootcamp.dto.response;

import br.com.bootcamp.model.NovaProposta;
import br.com.bootcamp.model.enums.StatusProposta;

public class PropostaResponse {

    private Long idProposta;
    private StatusProposta statusProposta;

    public PropostaResponse(NovaProposta proposta) {
        this.idProposta = proposta.getId();
        this.statusProposta = proposta.getStatusProposta();
    }

    public Long getId() {
        return idProposta;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }
}
