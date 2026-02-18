package com.micro.loans.service;

import com.micro.loans.dto.ResponseDto;

public interface ILoansService {

    /**
     * The below function will be implemented to create a Loan entry in the DB
     * @return
     */
    boolean createLoan();

    /**
     * The below function will be implemented to fetch the loan details on the basis of a given mobile Number;
     * @return
     */
    ResponseDto fetchLoan(String mobileNumber);


    /**
     * The below function will be implemented to update the loan entry in the DB
     * @return
     */
    boolean updateLoan();

    /**
     * The below function will be implemented to delete a loan entry in the DB
     * @return
     */
    boolean deleteLoan();
}
