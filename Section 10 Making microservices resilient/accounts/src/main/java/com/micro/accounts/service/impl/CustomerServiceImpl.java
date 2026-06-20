package com.micro.accounts.service.impl;


import com.micro.accounts.dto.*;
import com.micro.accounts.entity.Accounts;
import com.micro.accounts.entity.Customer;
import com.micro.accounts.exception.ResourceNotFoundException;
import com.micro.accounts.mapper.AccountsMapper;
import com.micro.accounts.mapper.CustomerMapper;
import com.micro.accounts.repository.AccountsRepository;
import com.micro.accounts.repository.CustomerRepository;
import com.micro.accounts.service.ICustomerService;
import com.micro.accounts.service.client.CardsFeignClient;
import com.micro.accounts.service.client.LoansFeignClient;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("CustomerDetailsServiceImplV1")
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {


    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final LoansFeignClient loansFeignClient;
    private final CardsFeignClient cardsFeignClient;

    /**
     *
     * @param mobileNumber
     * @return It will return customer details based on a mobileNumber.
     * This function will use the feignclient interfaces for loans and cards microservice to contact them and get the data.
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber , String correlationId) {


        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","Mobile Number",mobileNumber));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Account","CustomerId",customer.getCustomerId().toString()));

        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber, correlationId);
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCard(mobileNumber, correlationId);


        CustomerDetailsDto customerDetailsDto = new CustomerDetailsDto();
        if(loansDtoResponseEntity != null)
        {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        }
        if(cardsDtoResponseEntity!=null)
        {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }


        customerDetailsDto.setCustomerDto(customerDto);

        //print the logger statement:


        return customerDetailsDto;
    }
}
