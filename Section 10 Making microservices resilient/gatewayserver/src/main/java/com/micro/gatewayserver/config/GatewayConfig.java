package com.micro.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder)
    {
        return routeLocatorBuilder.routes()

                //Accounts Service Routing
                .route("accounts_route",
                        route->route.path("/microdemo/accounts/**")
                                .filters(f -> f.rewritePath("/microdemo/accounts/(?<segment>.*)",
                                        "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                        .circuitBreaker(config -> config.setName("accountsServiceCircuitBreaker")
                                                .setFallbackUri("forward:/accountsFallbackController")))
                                .uri("lb://ACCOUNTS"))

                //Loans service Routing
                .route("loans_route",
                route -> route.path("/microdemo/loans/**")
                        .filters(f -> f.rewritePath("/microdemo/loans/(?<segment>.*)",
                                "/${segment}")
                                .addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
                        .uri("lb://LOANS"))

                //Cards Service Routing
                .route("cards_route",
                        route -> route.path("/microdemo/cards/**")
                                .filters(f -> f.rewritePath("/microdemo/cards/(?<segment>.*)",
                                        "/${segment}")
                                        .addResponseHeader("X-Response-Header",LocalDateTime.now().toString()))
                                .uri("lb://CARDS"))

                .build();
    }

}
