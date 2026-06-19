package com.micro.gatewayserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/accountsFallbackController")
    public Mono<String> contactSupport()
    {
        return Mono.just("An error occurred . please try after sometime or contact the support team");
    }
}

