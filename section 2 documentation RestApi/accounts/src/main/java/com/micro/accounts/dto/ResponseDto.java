package com.micro.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

//This DTO is responsible for sending the status msg from the BE to the FE for any post request:
// No need to make validations here as we use this DTO to send info only and not to receive any ResponseBody
@Schema(
        name = "Response Schema ",
        description = "This Schema holds the HTTP Response information forwarded by the Server"
)
@Data@AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "Status code in the Response",
            example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message in the Response",
            example = "Request Processed Successfully"
    )
    private String statusMsg;
}
