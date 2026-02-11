package com.micro.accounts.service.impl;

import com.micro.accounts.constants.AccountsConstants;
import com.micro.accounts.dto.AccountsDto;
import com.micro.accounts.dto.CustomerDto;
import com.micro.accounts.entity.Accounts;
import com.micro.accounts.entity.Customer;
import com.micro.accounts.exception.CustomerAlreadyExistsException;
import com.micro.accounts.exception.ResourceNotFoundException;
import com.micro.accounts.mapper.AccountsMapper;
import com.micro.accounts.mapper.CustomerMapper;
import com.micro.accounts.repository.AccountsRepository;
import com.micro.accounts.repository.CustomerRepository;
import com.micro.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);
    //include the two repository classes:
    //you can also write @Autowired annotation instead of the AllArgsConstructor
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    //above the @AllArgsConstructor will actually create a object of this class and insert the two objects of the repo classes.
    /**
     *
     * @param customerDto is a object of type CustomerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        //first check if there is already a customer with the mobileNumber given in a new request

        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        //checking condition:
        if(optionalCustomer.isPresent())
        {
            throw new CustomerAlreadyExistsException("Customer is already registered with the given mobile number"
                    +customerDto.getMobileNumber());
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("AshishBanker");
        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }


    /**
     *
     * @param customer - takes a Customer object as input to create a new Accounts object and return to the caller
     *                 it is private function so it can only be called by the functions of this class only
     *
     * @returns The new account details.
     */
    private Accounts createNewAccount(Customer customer)
    {
        log.info("We are inside creating new Accounts");
        Accounts newAccount = new Accounts();

        newAccount.setCustomerId(customer.getCustomerId());

        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("AshishBanker");
        return newAccount;

    }




    /**
     *
     * @param mobileNumber This function will accept a string which is a mobileNumber
     *                     and then dig up the DB and look if there is any customer or not
     *                     if a customer is present then it will fetch the details from the DB and store it in the Customer object
     *                     and then it will map it back to the CustomerDto using the mapper functions
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
//        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
//
//        if(optionalCustomer.isEmpty())
//        {
//            throw new ResourceNotFoundException("Customer","Mobile Number",mobileNumber);
//        }

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","Mobile Number",mobileNumber));


        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Account","CustomerId",customer.getCustomerId().toString()));

        //Here we will use the mapper to map the Customer object and Accounts object to the CustomerDto object but
        // we donot have any mapper class of that type nor any DTO that combines both Customer and Accounts Object so let us do a simple
        //change that is we will include the Accounts object as a field inside the Customer class that will do the work.

        //mapping Customer to CustomerDto
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());

        //The below code is a setter for the AccountsDto and it expects a object of type AccountsDto in the parameter list
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        return customerDto;

    }
    }



