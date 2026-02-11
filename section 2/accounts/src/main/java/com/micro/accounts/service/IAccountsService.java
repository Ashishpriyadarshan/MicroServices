package com.micro.accounts.service;


import com.micro.accounts.dto.CustomerDto;


public interface IAccountsService {

    /**
     *
     * @param customerDto is a object of type CustomerDto
     */
    void createAccount(CustomerDto customerDto);


    /**
     *
     * @param mobileNumber This function will accept a string which is a mobileNumber
     *                     and then dig up the DB and look if there is any customer or not
     *                     if a customer is present then it will fetch the details from the DB and store it in the Customer object
     *                     and then it will map it back to the CustomerDto using the mapper functions
     * @return
     */
    CustomerDto fetchAccount(String mobileNumber);
}
