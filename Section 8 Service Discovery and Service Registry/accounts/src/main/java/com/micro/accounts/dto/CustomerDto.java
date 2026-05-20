package com.micro.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDto {

    @Schema(
            description = "Name of the Customer",
            example = "Ashish priyadarshan"
    )
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3,max=50,message = "The length of the customer name should be between 3 to 50 characters")
    private String name;


    @Schema(
            description = "Email address of the Customer",
            example = "ashish@xyz.com"
    )
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Email should be a valid value")
    private String email;

    @Schema(
            description = "Mobile Number of the Customer",
            example = "1234567890"
    )
    @NotEmpty(message = "Mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Accounts details of the Customer"
    )
    private AccountsDto accountsDto;
}
