package pl.lodz.p.it.ssbd2024.ssbd03.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@ControllerAdvice
public class RestControllerAdvice{

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
//    public void handleAuthenticationCredentialsNotFoundException() {}
}
