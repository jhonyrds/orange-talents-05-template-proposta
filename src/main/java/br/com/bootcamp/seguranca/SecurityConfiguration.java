package br.com.bootcamp.seguranca;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers(HttpMethod.GET, "/proposta/**")
        .hasAnyAuthority("SCOPE_propostas:read").antMatchers(HttpMethod.GET, "/cartao/***")
        .hasAnyAuthority("SCOPE_cartoes:read").antMatchers(HttpMethod.POST, "/cartao/**")
        .hasAnyAuthority("SCOPE_cartoes_write").antMatchers(HttpMethod.POST, "/proposta/**")
        .hasAnyAuthority("SCOPE_propostas:write").anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
