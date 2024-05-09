package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountConflictException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountStatusException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

/**
 * General exception handling component in a form of @ControllerAdvice for Account exceptions.
 */
@ControllerAdvice
public class AccountExceptionResolver {

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
     * This method is used to transform exceptions propagated from controller component, in a most basic way, since
     * exception message is extracted and inserted into HTTP Response Body as plain text, and returned with 400 BAD REQUEST
     * status code.
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

    /**
     * This method will transform any exception propagated from controller component to HTTP Response, if this exception
     * is either AccountStatusException or its subclass (where entire exception hierarchy is focused on the lack of permissions to
     * perform actions since status of the account does not allow it).
     *
     * @param accountStatusException Exception propagated from controller component, related to the lack of permissions
     *                               to perform some action due to the account status.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 412 PRECONDITION FAILED.
     */
    @ExceptionHandler(value = { AccountStatusException.class })
    public ResponseEntity<?> handleAccountStatusException(AccountStatusException accountStatusException) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
               .contentType(MediaType.TEXT_PLAIN)
               .body(accountStatusException.getMessage());
    }

    /**
     * Method used to transform AccountNotFound exception and its subclasses to HTTP Response 404 NOT FOUND, and will return
     * message, extracted from caught exception and put it into ResponseEntity as plaintext.
     *
     * @param accountNotFoundException AccountNotFound exception and its subclasses, related to not finding specified account object
     *                                 in the database.
     *
     * @return HTTP Response status 404 NOT FOUND.
     */
    @ExceptionHandler(value = { AccountNotFoundException.class })
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(accountNotFoundException.getMessage());
    }
}
