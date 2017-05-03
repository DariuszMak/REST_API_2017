package com.restapi2017.exceptions;

import com.restapi2017.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception exception) {
        return new ErrorMessage(exception.getMessage(), "Serwer ma gorsze chwile");
    }

    @ExceptionHandler({UserException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleUserException(UserException exception) {
        return new ErrorMessage(exception.getMessage(), exception.getUserMessage());
    }
}
