package com.micro.loans.service.implementation;

import com.micro.loans.constants.LoansConstants;
import com.micro.loans.dto.LoansDto;
import com.micro.loans.dto.ResponseDto;
import com.micro.loans.entity.Loans;
import com.micro.loans.exception.LoanAlreadyExistsException;
import com.micro.loans.exception.ResourceNotFoundException;
import com.micro.loans.mapper.LoansMapper;
import com.micro.loans.repository.LoansRepository;
import com.micro.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service("LoansServiceImplV1")
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {


    private final LoansRepository loansRepository;


    /**
     * The below function will be implemented to create a Loan entry in the DB
     * first it will run a JPA Query to check if a loan entry already exists or not in the DB if present then it will throw a
     * Runtime Exception otherwise it will proceed.
     *
     * @return
     */
    @Override
    public boolean createLoan(String mobileNumber)

    {

        if(loansRepository.findByMobileNumber(mobileNumber).isPresent())
        {
            throw new LoanAlreadyExistsException("Loan already exists with Mobile Number : "+mobileNumber);
        }



        return loansRepository.save(createNewLoan(mobileNumber))!=null ? true : false;
    }

    /**
     * The below function is private to the service class and cannot be accessed by anyone from outside
     * and this function will only get invoked when the above function executes its return statement.
     * @param mobileNumber
     * @return
     */
    private Loans createNewLoan(String mobileNumber)
    {
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

        return loans;
    }

    /**
     * The below function will be implemented to fetch the loan details on the basis of a given mobile Number;
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {

        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan","Mobile Number",mobileNumber));

            LoansDto loansDto = new LoansDto();

            loansDto= LoansMapper.loansToLoansDtoMapper(loans,loansDto);
            return loansDto;
    }

    /**
     * The below function will be implemented to update the loan entry in the DB
     *
     * @param loansDto
     * @return
     */
    @Override
    public boolean updateLoanDetails(LoansDto loansDto) {
        //first check if a loan already exists or Not in the Loan DB:
        Loans loans = loansRepository.findByMobileNumber(loansDto.getMobileNumber()).orElseThrow
                (()->new ResourceNotFoundException("Loan","Mobile Number",loansDto.getMobileNumber()));

        //map from LoansDto to Loans first

        loans = LoansMapper.loansDtoToLoansMapper(loansDto,loans);
        loansRepository.save(loans);
        return true;
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
