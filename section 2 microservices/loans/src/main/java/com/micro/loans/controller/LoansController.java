package com.micro.loans.controller;

import com.micro.loans.constants.LoansConstants;
import com.micro.loans.dto.ResponseDto;
import com.micro.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
