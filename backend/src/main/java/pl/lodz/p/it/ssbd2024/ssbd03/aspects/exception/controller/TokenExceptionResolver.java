package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;

/**
 * General exception handling component in a form of @ControllerAdvice for Token exceptions.
 */
@ControllerAdvice
public class TokenExceptionResolver {

    /**
     * This method will translate any TokenNotFoundException and its subclasses to HTTP Response status
     * 404 NOT FOUND and place message extracted from the exception in the response body.
     *
     * @param exception Instance of TokenNotFoundException or any of its subclasses, that is being caught, in order
     *                  to be transformed into HTTP Response.
     *
     * @return 404 NOT FOUND is thrown when this exception is caught when propagating from controller.
     */
    @ExceptionHandler(value = { TokenNotFoundException.class })
    public ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    /**
     * This method will translate any TokenNotValidException and its subclasses to HTTP Response status
     * 400 BAD REQUEST and place message extracted from the exception in the response body.
     *
     * @param exception Instance of TokenNotValidException or any of its subclasses, that is being caught, in order
     *                  to be transformed into HTTP Response.
     *
     * @return 400 BAD REQUEST is thrown when this exception is caught when propagating from controller.
     */
    @ExceptionHandler(value = { TokenNotValidException.class })
    public ResponseEntity<?> handleTokenNotValidException(TokenNotValidException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }
}