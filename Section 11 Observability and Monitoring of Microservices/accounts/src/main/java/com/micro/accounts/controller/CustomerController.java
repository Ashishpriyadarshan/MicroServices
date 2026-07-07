package com.micro.accounts.controller;

import com.micro.accounts.dto.CustomerDetailsDto;
import com.micro.accounts.dto.ErrorResposneDto;
import com.micro.accounts.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name="Rest API for Customers",
        description = "REST API to fetch the customer details"
)
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerController {

    @Qualifier(value = "CustomerDetailsServiceImplV1")
    private final ICustomerService iCustomerService;


    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Operation(
            summary = "Fetching the customer details",
            description = "This RestAPI is used inorder to fetch the consolidated details of a customer on the basis of a given mobile Number"+
                    "This API will return data of loans , cards , accounts and customer")
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

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("microdemo-correlation-id") String correlationId,
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
            String mobileNumber
    )
    {

        logger.debug("microdemo-correlation-id found: {}",correlationId);
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber , correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);
    }
}
