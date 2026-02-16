package com.micro.accounts.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3,max=50,message = "The length of the customer name should be between 3 to 50 characters")
    private String name;

    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Email should be a valid value")
    private String email;

    @NotEmpty(message = "Mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private AccountsDto accountsDto;
}
