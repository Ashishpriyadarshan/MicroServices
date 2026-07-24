package com.micro.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity)
    {
        serverHttpSecurity.authorizeExchange(exchange->
                exchange.pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/microdemo/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("/microdemo/cards/**").hasRole("CARDS")
                        .pathMatchers("/microdemo/loans/**").hasRole("LOANS"))
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec
                                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(
                                        new ReactiveJwtAuthenticationConverterAdapter(
                                                new KeycloakRoleConverter()
                                        )
                                )
                                )
                );

        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

}
