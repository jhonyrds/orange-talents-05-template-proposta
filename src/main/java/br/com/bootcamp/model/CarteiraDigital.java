package br.com.bootcamp.model;

import br.com.bootcamp.model.enums.TipoCarteira;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class CarteiraDigital {

    @Id
    @NotBlank
    private String id;

    @Email
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoCarteira carteira;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public CarteiraDigital(){}

    public CarteiraDigital(String id, String email, TipoCarteira carteira, Cartao cartao) {
        this.id = id;
        this.email = email;
        this.carteira = carteira;
        this.cartao = cartao;
    }
}
