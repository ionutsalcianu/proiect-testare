package ro.acs.ionut.proiecttestare.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @ApiModelProperty(required = true)
    private String requestUrl;

    @ApiModelProperty(required = true)
    private String errorMessage;
}