package com.micro.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Cards",
        description = "Schema to hold Card information"
)
public class CardsDto {

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    @NotEmpty(message = "Mobile Number cannot be null or empty")
    @Schema(
            description = "Mobile Number of the Card holder",example = "1234567890"
    )
    private String mobileNumber;

    @NotEmpty(message = "Card Number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{12})",message = "Card Number must be 12 digits")
    @Schema(
            description = "Card Number of the customer", example = "100646930341"
    )
    private String cardNumber;

    @NotEmpty(message = "Card Type cannot be null or empty")
    @Schema(
            description = "Type of the card", example = "Credit Card"
    )
    private String cardType;

    @Schema(
            description = "Total amount limit available against a card", example = "100000"
    )
    @Positive(message = "Total limit should be greater than zero")
    private int totalLimit;



    @PositiveOrZero(message = "Amount used should be equal or greater than zero")
    @Schema(
            description = "Total amount used by a Customer", example = "1000"
    )    private int amountUsed;

    @PositiveOrZero(message = "Available Amount should be equal or greater than zero")
    @Schema(
            description = "Total available amount against a card", example = "90000"
    )
    private int availableAmount;
}
