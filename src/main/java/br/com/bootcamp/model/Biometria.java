package br.com.bootcamp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    @Lob
    private String impressaoDigital;

    private LocalDateTime instanteCadastro;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String impressaoDigital) {
        this.impressaoDigital = impressaoDigital;
        this.uuid = UUID.randomUUID().toString();
        this.instanteCadastro = LocalDateTime.now();
    }

    public String getUuid() {
        return uuid;
    }
}
