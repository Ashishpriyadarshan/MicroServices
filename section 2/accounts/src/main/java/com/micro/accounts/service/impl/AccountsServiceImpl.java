package com.micro.accounts.service.impl;

import com.micro.accounts.constants.AccountsConstants;
import com.micro.accounts.dto.CustomerDto;
import com.micro.accounts.entity.Accounts;
import com.micro.accounts.entity.Customer;
import com.micro.accounts.exception.CustomerAlreadyExistsException;
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
}
