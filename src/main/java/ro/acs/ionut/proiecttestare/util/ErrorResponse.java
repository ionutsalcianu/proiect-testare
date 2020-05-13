package ro.acs.ionut.proiecttestare.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ErrorResponse {
    String url;
    String errorCode;
    String exception;
}