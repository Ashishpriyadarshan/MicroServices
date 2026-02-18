package com.micro.loans.service.implementation;

import com.micro.loans.constants.LoansConstants;
import com.micro.loans.dto.LoansDto;
import com.micro.loans.dto.ResponseDto;
import com.micro.loans.entity.Loans;
import com.micro.loans.repository.LoansRepository;
import com.micro.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Random;

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
    public boolean createLoan(String mobileNumber) {

        Loans loans = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        loans.setLoanNumber(Long.toString(randomLoanNumber));
        loans.setMobileNumber(mobileNumber);
        loans.setLoanType(LoansConstants.HOME_LOAN);
        loans.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        loans.setAmountPaid(0);
        loans.setOutstandingAmount(loans.getTotalLoan()-loans.getAmountPaid());
        loans.setCreatedAt(LocalDateTime.now());
        loans.setCreatedBy("Ashish");

        return loansRepository.save(loans)!=null ? true : false;
    }

    /**
     * The below function will be implemented to fetch the loan details on the basis of a given mobile Number;
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
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
