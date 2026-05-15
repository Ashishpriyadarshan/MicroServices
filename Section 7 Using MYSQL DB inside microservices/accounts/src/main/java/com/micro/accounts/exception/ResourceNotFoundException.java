package com.micro.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName,String fieldName , String fieldValue)
    {
        // we need to pass a single value to the constructor of the super class
        super(String.format("%s not found with the given input data %s : '%s'",resourceName,fieldName,fieldValue));
    }

}
