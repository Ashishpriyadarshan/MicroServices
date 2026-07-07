package com.micro.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Data@AllArgsConstructor
@Schema(
        name = "Error Response",
        description = "This Schema holds error details"
)
public class ErrorResponseDto {

    private String apiPath;

    private HttpStatus httpStatus;

    private String errorMessage;

    private LocalDateTime localDateTime;
}
