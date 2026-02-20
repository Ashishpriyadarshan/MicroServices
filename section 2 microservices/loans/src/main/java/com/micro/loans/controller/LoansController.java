package com.micro.loans.controller;

import com.micro.loans.constants.LoansConstants;
import com.micro.loans.dto.LoansDto;
import com.micro.loans.dto.ResponseDto;
import com.micro.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api" , produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class LoansController {




    @Qualifier(value = "LoansServiceImplV1")
    private final ILoansService iLoansService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam String mobileNumber)
    {
        if(iLoansService.createLoan(mobileNumber))
        {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(LoansConstants.STATUS_201,LoansConstants.MESSAGE_201));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto(LoansConstants.STATUS_500,LoansConstants.MESSAGE_500));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber)
    {

        LoansDto loansDto = new LoansDto();
        loansDto = iLoansService.fetchLoan(mobileNumber);

        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@RequestBody LoansDto loansDto)
    {
        if(iLoansService.updateLoanDetails(loansDto))
        {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseDto
                            (LoansConstants.STATUS_200,
                                    LoansConstants.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(LoansConstants.STATUS_417,LoansConstants.MESSAGE_417_UPDATE));
    }
}
