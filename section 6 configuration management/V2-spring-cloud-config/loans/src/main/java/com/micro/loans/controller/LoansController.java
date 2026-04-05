package com.micro.loans.controller;

import com.micro.loans.constants.LoansConstants;
import com.micro.loans.dto.ErrorResponseDto;
import com.micro.loans.dto.LoansContactInfo;
import com.micro.loans.dto.LoansDto;
import com.micro.loans.dto.ResponseDto;
import com.micro.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api" , produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(name = "CRUD RestApi's for Loans Microservices",
        description ="CRUD RestApi's for performing Create/Fetch/Update/Delete the Loan details"
)
public class LoansController {




    @Qualifier(value = "LoansServiceImplV1")
    private final ILoansService iLoansService;

    @Value("${app.name}")
    private String appName;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansContactInfo loansContactInfo;



    @PostMapping("/create")
    @Operation(
            summary = "Create Loan RestApi",
            description = "This Api end point is responsible for creating a Loan entry in the DB"
    )
    @ApiResponses(
            {
                    @ApiResponse
                            (
                                    responseCode = "201",
                                    description = "HTTP Status Created"
                            ),
                    @ApiResponse
                            (
                                    responseCode = "500",
                                    description = "HTTP Status Internal Server Error",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )

                            )
            }
            )
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                      @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
                                                      String mobileNumber)
    {
        if(iLoansService.createLoan(mobileNumber))
        {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(LoansConstants.STATUS_201,LoansConstants.MESSAGE_201));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto(LoansConstants.STATUS_500,LoansConstants.MESSAGE_500));
    }

    @GetMapping("/fetch")
    @Operation(
            summary = "Fetch Loan details",
            description = "This API EndPoint fetches loan details from the DB and it takes the mobile number as a parameter"
    )
    @ApiResponses
            (
                    {
                            @ApiResponse
                                    (
                                            responseCode = "200",
                                            description = "HTTP Status Code 200 OK"
                                    )
                    }
            )
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam
                                                         @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
                                                         String mobileNumber)
    {

        LoansDto loansDto = new LoansDto();
        loansDto = iLoansService.fetchLoan(mobileNumber);

        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @PutMapping("/update")
    @Operation(
            summary = "Update Loan Details",
            description = "This API EndPoint updates the Loan details"
    )
    @ApiResponses
            (
                    {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "HTTP Status Code OK"
                            ),
                            @ApiResponse(
                                    responseCode = "417",
                                    description = "HTTP Status Code Expectation Failed",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            )
                    }
            )
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid
                                                             @RequestBody
                                                             LoansDto loansDto)
    {
        if(iLoansService.updateLoanDetails(loansDto))
        {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseDto
                            (LoansConstants.STATUS_200,
                                    LoansConstants.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(LoansConstants.STATUS_417,LoansConstants.MESSAGE_417_UPDATE));
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete Loan Details ",
            description = "This API End-Point deletes the Loan details from the DB if it exists"
    )
    @ApiResponses
            (
                    {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "HTTP Status Code OK"
                            ),
                            @ApiResponse(
                                    responseCode = "417",
                                    description = "HTTP Status Code Expectation Failed",
                                    content = @Content
                                            (
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                            )
                    }
            )
    public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam
                                                             @Pattern(regexp ="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
                                                             String mobileNumber)
    {
        if(iLoansService.deleteLoan(mobileNumber))
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200,LoansConstants.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body( new ResponseDto( LoansConstants.STATUS_417,LoansConstants.MESSAGE_417_DELETE));
    }




    //----------------------------------------------------------------------------------------------------------------------------------
    @Operation(
            summary = "get-build-version  REST API",
            description = "This REST API is responsible to return the build version  which the controller gets from the config file using @Value annotation "
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
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                    }
            )
    @GetMapping("/get-build-version")
    public ResponseEntity<String> appBuildVersion()
    {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }



    @Operation(
            summary = "get-app-name  REST API",
            description = "This REST API is responsible to return the app name which the controller gets from the config file using @Value annotation "
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
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                    }
            )
    @GetMapping("/get-app-name")
    public ResponseEntity<String> appNameInfo()
    {
        return new ResponseEntity<>(appName,HttpStatus.OK);
    }



    @Operation(
            summary = "get-java-version  REST API",
            description = "This REST API is responsible to return the env variable details which the controller gets from the config file " +
                    "using the object of Environment Interface provided by " +
                    "the org.springframework.core.env.Environment"
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
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                    }
            )
    @GetMapping("/get-java-version")
    public ResponseEntity<String> envVariableInfo()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(
            summary = "get-mvn-version  REST API",
            description = "This REST API is responsible to return the env variable details which the controller gets from the config file " +
                    "using the object of Environment Interface provided by " +
                    "the org.springframework.core.env.Environment"
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
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                    }
            )
    @GetMapping("/get-mvn-version")
    public ResponseEntity<String> envMVN()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(environment.getProperty("MAVEN_HOME"));
    }


    @Operation(
            summary = "get-contact-info  REST API",
            description = "This REST API is responsible to return the details which the controller gets from " +
                    "POJO of the class having the ConfigurationProperties annotations" +
                    " The values as created in the application.properties yml file under the prefix name loans"+
                    " and class created with the ConfigurationProperties takes the prefix value as loans thats how it maps the values to " +
                    "the object of the class LoansContactInfo"
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
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                    }
            )
    @GetMapping("/get-contact-info")
    public ResponseEntity<LoansContactInfo> getContactInfo()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(loansContactInfo);
    }

}
