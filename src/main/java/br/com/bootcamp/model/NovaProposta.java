package br.com.bootcamp.model;

import br.com.bootcamp.dto.request.AnaliseRequest;
import br.com.bootcamp.dto.response.PropostaResponse;
import br.com.bootcamp.model.enums.StatusProposta;
import br.com.bootcamp.repository.AnaliseSolicitacaoClient;
import br.com.bootcamp.util.CPFOrCNPJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    public void realizarAnalise(AnaliseSolicitacaoClient analiseDeSolicitacaoClient) throws JsonMappingException, JsonProcessingException {
        try {
            AnaliseRequest analiseRequest = new AnaliseRequest(documento, nome,
                    idProposta.toString());
            PropostaResponse propostaResponse = analiseDeSolicitacaoClient.analisarProposta(analiseRequest);
            if (propostaResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
                statusProposta = StatusProposta.ELEGIVEL;
            }
        } catch (FeignException e) {
            PropostaResponse propostaResponse = new ObjectMapper().readValue(e.contentUTF8(),
                    PropostaResponse.class);
            if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()
                    && propostaResponse.getResultadoSolicitacao().equals("COM_RESTRICAO")) {
                statusProposta = StatusProposta.NAO_ELEGIVEL;
            }
        }
    }

}
