package com.micro.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data@AllArgsConstructor
public class LoansDto {


    private String mobileNumber;


    private String loanNumber;


    private String loanType;


    private Long totalLoan;


    private Long amountPaid;


    private Long outstandingAmount;

}
