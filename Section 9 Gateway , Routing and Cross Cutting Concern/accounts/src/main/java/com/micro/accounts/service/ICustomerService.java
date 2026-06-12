package com.micro.accounts.service;

import com.micro.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {


    /**
     *
     * @param mobileNumber
     * @return It will return customer details based on a mobileNumber.
     * This function will use the feignclient interfaces for loans and cards microservice to contact them and get the data.
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
