package com.micro.accounts.exception;

import com.micro.accounts.dto.ErrorResposneDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResposneDto> handleGlobalException(Exception exception,
                                                                                 WebRequest webRequest)
    {
        ErrorResposneDto errorResposneDto = new ErrorResposneDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResposneDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResposneDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception,
                                                                                 WebRequest webRequest)
    {
        ErrorResposneDto errorResposneDto = new ErrorResposneDto(
        webRequest.getDescription(false),
        HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResposneDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResposneDto> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                            WebRequest webRequest)
    {
        ErrorResposneDto errorResposneDto = new ErrorResposneDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                resourceNotFoundException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResposneDto, HttpStatus.NOT_FOUND);
    }
}
