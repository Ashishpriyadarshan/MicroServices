package com.micro.accounts.service;


import com.micro.accounts.dto.CustomerDto;


public interface IAccountsService {

    /**
     *
     * @param customerDto is a object of type CustomerDto
     */
    void createAccount(CustomerDto customerDto);
}
