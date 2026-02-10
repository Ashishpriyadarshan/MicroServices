package com.micro.accounts.controller;

import com.micro.accounts.constants.AccountsConstants;
import com.micro.accounts.dto.CustomerDto;
import com.micro.accounts.dto.ResponseDto;
import com.micro.accounts.entity.Accounts;
import com.micro.accounts.service.IAccountsService;
import com.micro.accounts.service.impl.AccountsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
//In production ready applications it is always advised to have a prefix path to your controller class like we have /api and also
//mention the return type of this controller means what type of return it give : produces means what it gives back
//MediaType from org.springframework.http.MediaType then APPLICATION_JSON_VALUE means it will always return a json type value
public class AccountsController {


    private AccountsServiceImpl accountsServiceImpl;

    @PostMapping("/create")
    //we need PostMapping as this is a create operation
     //RequestBody has to be linked to the CustomerDto type POJO as it is the class responsible for carrying the customer details
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto)
    {

        accountsServiceImpl.createAccount(customerDto);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
        //if you want you can directly return the ResponseDto object also but since your client is expecting a http request so they will get a body
        //But they will never get the overall status of their request like your reponse will lack the HttpStatus codes so it is not a good practice.
    }

}
