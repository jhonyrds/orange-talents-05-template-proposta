package br.com.bootcamp.model;

import br.com.bootcamp.dto.request.BloqueioCartaoRequest;
import br.com.bootcamp.interfaces.CartaoClient;
import br.com.bootcamp.model.enums.StatusCartao;
import feign.FeignException;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String idCartao;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Biometria> biometrias;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Bloqueio> bloqueios;

    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<AvisoViagem> avisosViagem;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String idCartao) {
        this.idCartao = idCartao;
        this.biometrias = new ArrayList<>();
        this.uuid = UUID.randomUUID().toString();
        this.avisosViagem = new ArrayList<>();
    }

    public String getIdCartao() {
        return idCartao;
    }

    public void cadastrarBiometria(Biometria biometria) {
        biometrias.add(biometria);

    }

    public HttpStatus realizarBloqueioClient(CartaoClient cartaoClient) {
        try {
            cartaoClient.bloquearCartao(idCartao, new BloqueioCartaoRequest("Sistema de Propostas"));
            return HttpStatus.OK;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value())
                return HttpStatus.UNPROCESSABLE_ENTITY;
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    public void adicionaDadosDeBloqueio(Bloqueio bloqueio) {
        statusCartao = StatusCartao.BLOQUEADO;
        bloqueios.add(bloqueio);
    }

    public void cadastrarAvisoViagem(AvisoViagem avisoViagem) {
        this.avisosViagem.add(avisoViagem);
    }
}
