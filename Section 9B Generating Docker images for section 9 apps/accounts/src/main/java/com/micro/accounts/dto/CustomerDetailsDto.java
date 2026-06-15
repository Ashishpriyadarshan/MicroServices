package com.micro.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
@Schema(
        name = "CustomerDetails",
        description = "Schema to hold Customer , Accounts , Cards and Loans Data of a Customer"
)
public class CustomerDetailsDto {



    @Schema(
            description = "This will hold the customer information as well as the Accounts information"
    )
    CustomerDto customerDto;

    @Schema(
            description = "This will hold the cards information"
    )
    CardsDto cardsDto;

    @Schema(
            description = "This will hold the loans details"
    )
    LoansDto loansDto;
}
