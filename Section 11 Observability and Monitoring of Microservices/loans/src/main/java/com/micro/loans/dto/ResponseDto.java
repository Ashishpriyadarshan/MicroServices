package com.micro.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
@Schema
        (
                name = "Response Schema",
                description = "This Schema holds Http Response details"
        )
public class ResponseDto {

    private String statusCode;

    private String message;
}

