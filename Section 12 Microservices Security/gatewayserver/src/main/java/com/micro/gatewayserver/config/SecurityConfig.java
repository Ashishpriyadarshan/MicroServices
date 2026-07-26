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
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.cors(Customizer.withDefaults());

        serverHttpSecurity.authorizeExchange(exchange -> exchange

                        // ---------------- SECURED GET APIs ----------------
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.GET,"/microdemo/accounts/api/get-contact-info",
                                "/microdemo/loans/api/get-contact-info",
                                "/microdemo/cards/api/get-contact-info").permitAll()
                        .pathMatchers(HttpMethod.GET,
                                "/microdemo/accounts/api/fetch",
                                "/microdemo/accounts/api/fetchCustomer")
                        .hasRole("ACCOUNTS")

                        .pathMatchers(HttpMethod.GET,
                                "/microdemo/cards/api/fetch")
                        .hasRole("CARDS")

                        .pathMatchers(HttpMethod.GET,
                                "/microdemo/loans/api/fetch")
                        .hasRole("LOANS")

                        // ---------------- Other secured APIs ----------------

                        .pathMatchers("/microdemo/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("/microdemo/cards/**").hasRole("CARDS")
                        .pathMatchers("/microdemo/loans/**").hasRole("LOANS")

                        // ---------------- Public GET APIs ----------------

                        .pathMatchers(HttpMethod.GET).permitAll()

                        // Everything else must be authenticated
                        .anyExchange().authenticated()

                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new ReactiveJwtAuthenticationConverterAdapter(
                                        new KeycloakRoleConverter()
                                )
                        ))
                );

        serverHttpSecurity.csrf(csrf -> csrf.disable());

        return serverHttpSecurity.build();
    }
}