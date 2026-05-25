package com.micro.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Cards",
        description = "Schema to hold Cards information , This Schema is used as a return type for one of the API's of the Cards microservice"+
                " Which is invoked by our Account microservice inorder to get the Cards details as per the mobileNumber"
)
public class CardsDto {


    @Schema(
            description = "Mobile Number of the Card holder",example = "1234567890"
    )
    private String mobileNumber;

    @Schema(
            description = "Card Number of the customer", example = "100646930341"
    )
    private String cardNumber;

    @Schema(
            description = "Type of the card", example = "Credit Card"
    )
    private String cardType;

    @Schema(
            description = "Total amount limit available against a card", example = "100000"
    )
    private int totalLimit;


    @Schema(
            description = "Total amount used by a Customer", example = "1000"
    )    private int amountUsed;


    @Schema(
            description = "Total available amount against a card", example = "90000"
    )
    private int availableAmount;
}
