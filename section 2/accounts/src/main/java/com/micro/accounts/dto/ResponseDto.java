package com.micro.accounts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

//This DTO is responsible for sending the status msg from the BE to the FE for any post request:
@Data@AllArgsConstructor
public class ResponseDto {

    private String statusCode;

    private String statusMsg;
}
