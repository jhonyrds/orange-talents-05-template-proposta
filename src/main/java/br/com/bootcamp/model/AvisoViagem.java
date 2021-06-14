package br.com.bootcamp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinoViagem;

    private LocalDate dataTerminoViagem;

    private String ipCliente;

    private String userAgentCliente;

    @ManyToOne
    private Cartao cartao;

    private LocalDateTime instanteAviso;

    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(String destinoViagem, LocalDate dataTerminoViagem,
                       String ipCliente, String userAgentCliente, Cartao cartao) {
        this.destinoViagem = destinoViagem;
        this.dataTerminoViagem = dataTerminoViagem;
        this.ipCliente = ipCliente;
        this.userAgentCliente = userAgentCliente;
        this.cartao = cartao;
        this.instanteAviso = LocalDateTime.now();
    }
}
