package com.micro.gatewayserver.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder; // ADDED: Required for builder
import org.springframework.cloud.client.circuitbreaker.Customizer; // FIXED: Changed from java.beans.Customizer
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {


    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> accountsRouteTimeLimiterCustomizer()
    {
        return factory -> factory.configure(builder ->builder
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(5))
                        .build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build(),"accountsServiceCircuitBreaker");
    }


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
                                        .addResponseHeader("X-Response-Header",LocalDateTime.now().toString())
                                        .retry(retryConfig -> retryConfig.setRetries(3)
                                                .setMethods(HttpMethod.GET)
                                                .setStatuses(HttpStatus.REQUEST_TIMEOUT, HttpStatus.SERVICE_UNAVAILABLE,HttpStatus.INTERNAL_SERVER_ERROR)
                                                .setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true)))
                                .uri("lb://CARDS"))

                .build();
    }

}
