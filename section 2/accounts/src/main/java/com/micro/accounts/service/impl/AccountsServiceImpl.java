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

@Service("accountsServiceImplV1")
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

    /**
     *
     * @param customerDto takes a object of type CustomerDto and then triggers a update/PUT request in the DB
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated=false;

        AccountsDto accountsDto = customerDto.getAccountsDto();
        //check if account is present in DB or not:
        if(accountsDto!=null) {
            Accounts accounts = accountsRepository.findByAccountNumber(accountsDto.getAccountNumber()).
                    orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString()));


             AccountsMapper.maptoAccounts(accountsDto,accounts);
             accounts=accountsRepository.save(accounts);

             Long customerId = accounts.getCustomerId();
             Customer customer = customerRepository.findById(customerId).orElseThrow
                     (()->new ResourceNotFoundException("Customer","CustomerId",customerId.toString()));

             CustomerMapper.mapToCustomer(customerDto,customer);
             customerRepository.save(customer);

             isUpdated=true;
        }
        return isUpdated;
    }

    /**
     * The below function will take the mobileNumber as a input from the controller . The Api endpoint will receive a mobileNumber.
     * and on the basis of the mobile number first check if the Resource is present in both the Accounts and Customer table or not
     * if they are present then delete the records belonging to that particular mobile number
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {

        //first check if any record exists or not.
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","Mobile Number",mobileNumber));

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.delete(customer);

        return true;
    }
}



