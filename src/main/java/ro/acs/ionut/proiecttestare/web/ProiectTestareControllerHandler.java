package ro.acs.ionut.proiecttestare.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ro.acs.ionut.proiecttestare.util.ErrorCode;
import ro.acs.ionut.proiecttestare.util.ErrorResponse;
import ro.acs.ionut.proiecttestare.util.PublicHolidayAlreadyExists;
import ro.acs.ionut.proiecttestare.util.PublicHolidaysNotFound;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@ControllerAdvice
@ResponseBody
public class ProiectTestareControllerHandler {

    @ExceptionHandler(PublicHolidaysNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handledPublicHolidayNotFoundException(HttpServletRequest request, PublicHolidaysNotFound exception) {
        return toErrorResponse(request, ErrorCode.PUBLIC_HOLIDAYS_NOT_FOUND, exception);
    }
    @ExceptionHandler(PublicHolidayAlreadyExists.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handledPublicHolidayAlreadyExistsException(HttpServletRequest request, PublicHolidayAlreadyExists exception) {
        return toErrorResponse(request, ErrorCode.PUBLIC_HOLIDAY_ALREADY_EXISTS, exception);
    }

    private static ErrorResponse toErrorResponse(HttpServletRequest request, ErrorCode errorCode, Exception exception) {
        return new ErrorResponse(request.getRequestURL().toString(),
                errorCode.name(),
                exception.getMessage());
    }
}
