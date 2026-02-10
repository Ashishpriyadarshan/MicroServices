package com.micro.accounts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

//THis DTO is responsible to create and send a responsebody with all the error details to the client
@Data@AllArgsConstructor
public class ErrorResposneDto {

    private String apiPath;

    private HttpStatus errorCode;

    private String errorMessage;

    private LocalDateTime errorTime;
}
