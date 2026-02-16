package com.micro.accounts.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

    @NotEmpty(message = "Account number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Account number must be 10 digits")
    private Long accountNumber;

    @NotEmpty(message = "Account Type cannot be empty or null")
    private String accountType;

    @NotEmpty(message = "Branch address cannot be empty or null")
    private String branchAddress;
}
