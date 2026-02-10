package com.micro.accounts.controller;

import com.micro.accounts.entity.Accounts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @GetMapping("/sayHello")
    public String sayHello()
    {

        return "Hello Sir";
    }
}
