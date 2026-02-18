package com.micro.loans.service.implementation;

import com.micro.loans.dto.ResponseDto;
import com.micro.loans.repository.LoansRepository;
import com.micro.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service("LoansServiceImplV1")
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {


    private final LoansRepository loansRepository;
    /**
     * The below function will be implemented to create a Loan entry in the DB
     *
     * @return
     */
    @Override
    public boolean createLoan() {
        return false;
    }

    /**
     * The below function will be implemented to fetch the loan details on the basis of a given mobile Number;
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public ResponseDto fetchLoan(String mobileNumber) {
        return null;
    }

    /**
     * The below function will be implemented to update the loan entry in the DB
     *
     * @return
     */
    @Override
    public boolean updateLoan() {
        return false;
    }

    /**
     * The below function will be implemented to delete a loan entry in the DB
     *
     * @return
     */
    @Override
    public boolean deleteLoan() {
        return false;
    }

}
