package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.integrity.AccountDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountConflictException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.ResetOwnPasswordException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountStatusException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

/**
 * General exception handling component in a form of @ControllerAdvice for Account exceptions.
 */
@ControllerAdvice
@Order(1)
public class AccountExceptionResolver {

    /**
     * This method is used to transform AccountConstraintViolationException, which could be thrown when user account is created or modified,
     * that is being propagated from controller component into HTTP response, so that exception is handled without container intervention.
     *
     * @param accountConstraintViolationException Exception that will be processed into HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {AccountConstraintViolationException.class})
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
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 409 CONFLICT.
     */
    @ExceptionHandler(value = {AccountConflictException.class})
    public ResponseEntity<?> handleAccountConflictException(AccountConflictException accountConflictException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(accountConflictException));
    }

    /**
     * This method is used to transform any AccountNotFoundException or exception that extend it, which could be thrown when
     * wanted account is not available in database. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountNotFoundException AccountNotFoundException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {AccountNotFoundException.class})
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(accountNotFoundException));
    }

    /**
     * This method will transform any exception propagated from controller component to HTTP Response, if this exception
     * is either AccountStatusException or its subclass (where entire exception hierarchy is focused on the lack of permissions to
     * perform actions since status of the account does not allow it).
     *
     * @param accountStatusException Exception propagated from controller component, related to the lack of permissions
     *                               to perform some action due to the account status.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 412 PRECONDITION FAILED.
     */
    @ExceptionHandler(value = {AccountStatusException.class})
    public ResponseEntity<?> handleAccountStatusException(AccountStatusException accountStatusException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(accountStatusException));
    }

    /**
     * This method is used to transform any AccountDataIntegrityCompromisedException or exception that extend it, which could be thrown when
     * the request contains an account object with modified signature-protected fields. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountDataIntegrityCompromisedException AccountDataIntegrityCompromisedException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {AccountDataIntegrityCompromisedException.class})
    public ResponseEntity<?> handleAccountDataIntegrityCompromisedException(AccountDataIntegrityCompromisedException accountDataIntegrityCompromisedException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(accountDataIntegrityCompromisedException));
    }

    /**
     * This method is used to transform any ResetOwnPasswordException or exception that extends it. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param resetOwnPasswordException ResetOwnPasswordException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST.
     */
    @ExceptionHandler(value = {ResetOwnPasswordException.class})
    public ResponseEntity<?> handleResetOwnPasswordException(ResetOwnPasswordException resetOwnPasswordException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(resetOwnPasswordException));
    }

    /**
     * This method handles InvalidLoginException, which is thrown during login attempt when either given credentials
     * are invalid or user account in not found in the database (it is a safety measure not to "scan" for logins that
     * have accounts associated with them).
     *
     * @return 401 UNAUTHORIZED is returned when this exception is caught while propagating
     * from controller component.
     */
    @ExceptionHandler(value = {InvalidLoginAttemptException.class})
    public ResponseEntity<?> handleInvalidLoginAttemptException(InvalidLoginAttemptException invalidLoginAttemptException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(invalidLoginAttemptException));
    }

    /**
     * This method is used to transform any AccountUserLevelException or exception that extends it. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountUserLevelException AccountUserException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST.
     */
    @ExceptionHandler(value = {AccountUserLevelException.class})
    public ResponseEntity<?> handleAccountUserLevelException(AccountUserLevelException accountUserLevelException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(accountUserLevelException));
    }
}
