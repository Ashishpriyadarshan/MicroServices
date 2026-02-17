package com.micro.accounts.controller;

import com.micro.accounts.constants.AccountsConstants;
import com.micro.accounts.dto.CustomerDto;
import com.micro.accounts.dto.ErrorResposneDto;
import com.micro.accounts.dto.ResponseDto;
import com.micro.accounts.entity.Accounts;
import com.micro.accounts.service.IAccountsService;
import com.micro.accounts.service.impl.AccountsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name="CRUD REST API's for Accounts microservice",
        description = "CRUD REST API's for creating/fetching/updating/deleting the customer and accounts data"
)
//In production ready applications it is always advised to have a prefix path to your controller class like we have /api and also
//mention the return type of this controller means what type of return it give : produces means what it gives back
//MediaType from org.springframework.http.MediaType then APPLICATION_JSON_VALUE means it will always return a json type value
public class AccountsController {


    @Qualifier("accountsServiceImplV1")
    private IAccountsService accountsServiceImpl;



    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create and register new customer and account details "
    )
    @ApiResponse
            (
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            )
    @PostMapping("/create")
    //we need PostMapping as this is a create operation
     //RequestBody has to be linked to the CustomerDto type POJO as it is the class responsible for carrying the customer details
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto)
    {

        accountsServiceImpl.createAccount(customerDto);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
        //if you want you can directly return the ResponseDto object also but since your client is expecting a http request so they will get a body
        //But they will never get the overall status of their request like your reponse will lack the HttpStatus codes so it is not a good practice.
    }


    /**
     *
     * @param mobileNumber since we are using RequestParam thats why the API request is going to be /fetch?mobileNumber=1234567890
     *                     but if it was PathVariable then the URL would have looked like /fetch/{mobileNumber}
     * @return
     */

    @Operation(
            summary = "Fetch Customer and Account details from the DB",
            description = "This REST API is responsible to return the Customer and Account details from the DB based on a Mobile Number sent as" +
                    "RequestParam/QueryParam"
    )
    @ApiResponse
            (
                    responseCode = "200",
                    description = "HTTP Status OK"
            )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @NotEmpty(message = "Mobile number cannot be empty")
                                                               @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                               String mobileNumber)
    {
        CustomerDto customerDto = accountsServiceImpl.fetchAccount(mobileNumber);

        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }



    @Operation(
            summary = "Update Account details REST API",
            description = "This REST API is responsible to update the Account details of a customer based on the provided Account Number " +
                    " you have to pass both the customer as well as account details in a single JSON format"
    )
    @ApiResponses
            (
                    {
                            @ApiResponse
                                    (
                                            responseCode = "200",
                                            description = "HTTP Status OK"
                                    ),
                            @ApiResponse
                                    (
                                            responseCode = "500",
                                            description = "HTTP Status Internal Server Error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResposneDto.class)
                                                    //we included the schema here because ErrorResponseDto only gets invoked when an exception is thrown so
                                                    //by default it is not scanned by OpenAPI just like other controller DTO's
                                                    //Now it will get scanned and can be seen in the swagger-ui
                                            )

                                    )
                    }
            )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid@RequestBody CustomerDto customerDto)
    {
        boolean isUpdated = accountsServiceImpl.updateAccount(customerDto);
        if(isUpdated)
        {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500,AccountsConstants.MESSAGE_500));
        }
    }





    @Operation(
            summary = "Delete Account details REST API",
            description = "This REST API is responsible to Delete the Customer and Account details of a customer based on the provided Account Number " +
                    " you have to pass both the customer as well as account details in a single JSON format"
    )
    @ApiResponses
            (
                    {
                            @ApiResponse
                                    (
                                            responseCode = "200",
                                            description = "HTTP Status OK"
                                    ),
                            @ApiResponse
                                    (
                                            responseCode = "500",
                                            description = "HTTP Status Internal Server Error"
                                    )
                    }
            )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @NotEmpty(message = "Mobile number cannot be empty")
                                                                @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber)
    {
        boolean isDeleted = accountsServiceImpl.deleteAccount(mobileNumber);
        if(isDeleted)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500,AccountsConstants.MESSAGE_500));
        }
    }
}
