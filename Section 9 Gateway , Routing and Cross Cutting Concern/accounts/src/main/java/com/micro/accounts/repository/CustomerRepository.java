package com.micro.accounts.repository;

import com.micro.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    //we are using optional here because for a given mobile number there can either be a object of type Customer or a NULL Object
    Optional<Customer> findByMobileNumber(String mobileNumber);
}
