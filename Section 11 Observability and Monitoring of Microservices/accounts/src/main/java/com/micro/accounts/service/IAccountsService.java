package com.micro.accounts.service;


import com.micro.accounts.dto.CustomerDto;
import com.micro.accounts.entity.Customer;


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


    /**
     *
     * @param customerDto takes a object of type CustomerDto and then triggers a update/PUT request in the DB
     *                    you can change whatever field you want but not the AccountNumber .
     *                    You can change any field of the Customer or Accounts Entity other than the AccountNumber
     * @return
     */
    boolean updateAccount(CustomerDto customerDto);


    /**
     * The below function will take the moboleNumber as a input from the controller . The Api endpoint will receive a mobileNumber.
     * and on the basis of the mobile number first check if the Resource is present in both the Accounts and Customer table or not
     *  if they are present then delete the records belonging to that particular mobile number
     * @param mobileNumber
     * @return
     */
    public boolean deleteAccount(String mobileNumber);
}
