package com.micro.loans.repository;

import com.micro.loans.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoansRepository  extends JpaRepository<Loans,Long> {

    /**
     *  The below function is a custom function which will be used to fetch the loans details on the basis of a given mobile Number
     *  if there is no loan entry then it will return NULL thats why we have Optional as return type
     * @param mobileNumber
     * @return
     */
    Optional<Loans> findByMobileNumber(String mobileNumber);
}
