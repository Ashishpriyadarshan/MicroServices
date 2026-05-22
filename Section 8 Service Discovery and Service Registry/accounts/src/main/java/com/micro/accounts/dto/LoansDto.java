package com.micro.accounts.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data@AllArgsConstructor@NoArgsConstructor
@Schema(
        name = "Loans Schema",
        description = "This schema holds Loans Details"+
                " we are using this schema which will recieve the incoming data from the Loans app when the fetchLoansDetails is used "+
                "For the interservices communication"
)
public class LoansDto {

    private String mobileNumber;
    private String loanNumber;
    private String loanType;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;

}
