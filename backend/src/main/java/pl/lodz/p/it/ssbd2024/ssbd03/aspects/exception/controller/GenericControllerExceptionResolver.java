package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountConflictException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

@ControllerAdvice
public class GenericControllerExceptionResolver {

    /**
     * This method is used to transform AccountConstraintViolationException, which could be thrown when user account is created or modified,
     * that is being propagated from controller component into HTTP response, so that exception is handled without container intervention.
     *
     * @param accountConstraintViolationException Exception that will be processed into HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = { AccountConstraintViolationException.class })
    public ResponseEntity<?> handleAccountConstraintViolationException(AccountConstraintViolationException accountConstraintViolationException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountConstraintViolationExceptionDTO(accountConstraintViolationException));
    }

    /**
     * This method is used to transform any AccountConflictException or exception that extend it, which could be thrown when
     * trying to create a new account with login / email already taken. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountConflictException AccountConflictException that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = { AccountConflictException.class })
    public ResponseEntity<?> handleAccountConflictException(AccountConflictException accountConflictException) {
        return ResponseEntity.badRequest()
               .contentType(MediaType.TEXT_PLAIN)
               .body(accountConflictException.getMessage());
    }
}
