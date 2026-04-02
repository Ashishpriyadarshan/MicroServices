package com.micro.cards.controller;

import com.micro.cards.constants.CardsConstant;
import com.micro.cards.dto.CardsContactInfoDto;
import com.micro.cards.dto.CardsDto;
import com.micro.cards.dto.ErrorResponseDto;
import com.micro.cards.dto.ResponseDto;
import com.micro.cards.entity.Cards;
import com.micro.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(
        name = "CURD RestApi's for the Cards microservices",
        description =" CRUD RestApi's of cards microservices for creating/fetching/updating/deleting the records"
)
public class CardsController {

    @Qualifier("CardsServiceImplV1")
    private final ICardsService iCardsService;

    @Value("${app.name}")
    private String appName;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;


    @PostMapping("/create")
    @Operation(
            summary = "Create Card REST API",
            description = "This API End point is responsible for creating a card record in the DB and it takes a String mobile Number as a parameter"

    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "201",
                            description = "HTTP Status CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status Internal Server Error",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponseDto> createCard(@RequestParam
                                                  @Pattern(regexp = "[0-9]{10}",message = "Mobile Number must be 10 digits long")
                                                      String mobileNumber)
    {
        iCardsService.createCard(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstant.STATUS_201,CardsConstant.MESSAGE_201));
    }


    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch card details based on a mobile number which is a string of 10 digits"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestParam
                                                  @Pattern(regexp ="[0-9]{10}",message = "Mobile number must be 10 digits long")
                                                   String mobileNumber)
    {

        CardsDto cardsDto =  iCardsService.fetchCardDetails(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cardsDto);
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update card details based on the entire details of a card either in the form of Card Schema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@RequestBody @Valid CardsDto cardsDto)
    {
        if(iCardsService.updateCardDetails(cardsDto))
        {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseDto
                            (CardsConstant.STATUS_200,CardsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(CardsConstant.STATUS_417,CardsConstant.MESSAGE_417_UPDATE));
    }


    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number which is a string of 10 digits"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                             @Pattern(regexp = "[0-9]{10}",message = "Mobile Number must be 10 digits long")
                                                              String mobileNumber)
    {
        boolean isDeleted = iCardsService.deleteCardDetails(mobileNumber);

        if(isDeleted)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstant.STATUS_200,CardsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(CardsConstant.STATUS_417, CardsConstant.MESSAGE_417_DELETE));
    }



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
                    " The values as created in the application.properties yml file under the prefix name cards"+
                    " and class created with the ConfigurationProperties takes the prefix value as cards thats how it maps the values to " +
                    "the object of the class CardsContactInfoDto"
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
    public ResponseEntity<CardsContactInfoDto> getContactInfo()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }

}
