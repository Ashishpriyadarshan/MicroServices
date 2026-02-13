package com.micro.accounts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

//This DTO is responsible for sending the status msg from the BE to the FE for any post request:
// No need to make validations here as we use this DTO to send info only and not to receive any ResponseBody
@Data@AllArgsConstructor
public class ResponseDto {

    private String statusCode;

    private String statusMsg;
}
