package com.micro.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "Schema to hold Account details of the Customer"
)
@Data
public class AccountsDto {

    @NotEmpty(message = "Account number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Account number must be 10 digits")
    @Schema(
            description = "Account Number of Customer"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account Type cannot be empty or null")
    @Schema(
            description = "Account Type of Bank Account", example = "Savings"
    )
    private String accountType;

    @NotEmpty(message = "Branch address cannot be empty or null")
    @Schema(
            description = "Bank Address of the Bank Account"
    )
    private String branchAddress;
}
