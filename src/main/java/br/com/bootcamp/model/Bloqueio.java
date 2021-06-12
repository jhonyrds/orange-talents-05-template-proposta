package br.com.bootcamp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime instanteBloqueio;

    private String ipCliente;

    private String userAgentCliente;

    @Deprecated
    public Bloqueio(){}

    public Bloqueio(String ipCliente, String userAgentCliente){
        this.ipCliente = ipCliente;
        this.userAgentCliente = userAgentCliente;
        this.instanteBloqueio = LocalDateTime.now();
    }
}
