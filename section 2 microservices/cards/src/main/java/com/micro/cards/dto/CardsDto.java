package com.micro.cards.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardsDto {

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    @NotEmpty(message = "Mobile Number cannot be null or empty")
    private String mobileNumber;

    @NotEmpty(message = "Card Number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{12})",message = "Card Number must be 12 digits")
    private String cardNumber;

    @NotEmpty(message = "Card Type cannot be null or empty")
    private String cardType;

    @Positive(message = "Total limit should be greater than zero")
    private int totalLimit;



    @PositiveOrZero(message = "Amount used should be equal or greater than zero")
    private int amountUsed;

    @PositiveOrZero(message = "Available Amount should be equal or greater than zero")
    private int availableAmount;
}
