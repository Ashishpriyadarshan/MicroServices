package com.micro.loans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data@AllArgsConstructor@NoArgsConstructor
public class LoansDto {


    @NotEmpty(message = "Mobile Number is required")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    private String mobileNumber;


    @NotEmpty(message = "Loan Number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{12})",message = "Loan Number must be 12 digits")
    private String loanNumber;


    @NotEmpty(message = "Loan Type cannot be Null or Empty")
    private String loanType;


    @PositiveOrZero(message = "Total Loan amount should be greater than 0")
    private int totalLoan;


    @PositiveOrZero(message = "Amount paid should be equal or greater than 0")
    private int amountPaid;


    @PositiveOrZero(message = "Total Outstanding amount should be equal or greater than zero")
    private int outstandingAmount;

}
