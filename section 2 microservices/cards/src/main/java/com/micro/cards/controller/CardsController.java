package com.micro.cards.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
public class CardsController {

    @GetMapping("/check")
    public ResponseEntity<String> check()
    {
        return ResponseEntity.status(200).body("Working Fine");
    }

    @GetMapping("/check1")
    public ResponseEntity<String> check1()
    {
        return new ResponseEntity<>("Working fine", HttpStatusCode.valueOf(200));
    }
}
