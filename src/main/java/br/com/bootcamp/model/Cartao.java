package br.com.bootcamp.model;

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
}
