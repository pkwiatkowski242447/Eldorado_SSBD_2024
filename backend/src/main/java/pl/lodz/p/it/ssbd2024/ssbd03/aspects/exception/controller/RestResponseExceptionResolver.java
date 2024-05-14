package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

@ControllerAdvice
public class RestResponseExceptionResolver extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.TEXT_PLAIN)
                .body(I18n.ACCESS_DENIED_EXCEPTION);
    }


}
