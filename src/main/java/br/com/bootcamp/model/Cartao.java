package br.com.bootcamp.model;

import br.com.bootcamp.dto.request.BloqueioCartaoRequest;
import br.com.bootcamp.interfaces.CartaoClient;
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

    private String numeroCartao;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Biometria> biometrias;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Bloqueio> bloqueios;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
        this.biometrias = new ArrayList<>();
        this.uuid = UUID.randomUUID().toString();
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void cadastrarBiometria(Biometria biometria) {
        biometrias.add(biometria);

    }

    public HttpStatus realizarBloqueioClient(CartaoClient cartaoClient) {
        try {
            cartaoClient.bloquearCartao(numeroCartao, new BloqueioCartaoRequest("Sistema de Propostas"));
            return HttpStatus.OK;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNPROCESSABLE_ENTITY.value())
                return HttpStatus.UNPROCESSABLE_ENTITY;
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    public void adicionaDadosDeBloqueio(Bloqueio bloqueio) {
        bloqueios.add(bloqueio);
    }
}
