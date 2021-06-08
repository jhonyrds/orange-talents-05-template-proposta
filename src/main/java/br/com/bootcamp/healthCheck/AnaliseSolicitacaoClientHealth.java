package br.com.bootcamp.healthCheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.net.URL;

@Component
public class AnaliseSolicitacaoClientHealth implements HealthIndicator {

    @Value("${analise.proposta}/solicitacao")
    private String url;

    @Override
    public Health health() {
        try (Socket socket = new Socket(new URL(url).getHost(), 9999)) {
        } catch (Exception e) {
            return Health.down().withDetail("Erro", e.getMessage()).build();
        }
        return Health.up().build();
    }
}
