package com.micro.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

//THis DTO is responsible to create and send a responsebody with all the error details to the client
// No need to make validations here as we use this DTO to send info only and not to receive any ResponseBody
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold Error Response Information"
)
@Data@AllArgsConstructor
public class ErrorResposneDto {

    @Schema(
            description = "API path invoked by client"
    )
    private String apiPath;

    @Schema(
            description = "Error code representing the error happened"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message representing the error that happened"
    )
    private String errorMessage;

    @Schema(
            description = "Time at which the Error occured"
    )
    private LocalDateTime errorTime;
}
