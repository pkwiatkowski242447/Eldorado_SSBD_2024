package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.AccountConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountConflictException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderException;

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
     * to HTTP Response with status code 409 CONFLICT
     */
    @ExceptionHandler(value = { AccountConflictException.class })
    public ResponseEntity<?> handleAccountConflictException(AccountConflictException accountConflictException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
               .contentType(MediaType.TEXT_PLAIN)
               .body(accountConflictException.getMessage());
    }

    /**
     * This method is used to transform any MapperBaseException or exception that extend it, which could be thrown when
     * trying to map not handled/ invalid data. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param mapperBaseException MapperBaseException that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = { MapperBaseException.class })
    public ResponseEntity<?> handleMapperBaseException(MapperBaseException mapperBaseException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(mapperBaseException.getMessage());
    }

    /**
     * This method is used to transform any AccountNotFoundException or exception that extend it, which could be thrown when
     * wanted account is not available in database. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountNotFoundException AccountNotFoundException that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {AccountNotFoundException.class})
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(accountNotFoundException.getMessage());
    }

    /**
     * This method is used to transform any InvalidRequestHeaderException or exception that extend it.
     * After such exception is propagated from controller it will be caught and transformed into HTTP Response.
     *
     * @param invalidRequestHeaderException InvalidRequestHeaderException that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {InvalidRequestHeaderException.class})
    public ResponseEntity<?> handleInvalidRequestHeaderException(InvalidRequestHeaderException invalidRequestHeaderException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(invalidRequestHeaderException.getMessage());
    }

    /**
     * This method is used to transform any AccountDataIntegrityCompromisedException or exception that extend it, which could be thrown when
     * the request contains an account object with modified signature-protected fields. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param accountDataIntegrityCompromisedException AccountDataIntegrityCompromisedException that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {AccountDataIntegrityCompromisedException.class})
    public ResponseEntity<?> handleAccountDataIntegrityCompromisedException(AccountDataIntegrityCompromisedException accountDataIntegrityCompromisedException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(accountDataIntegrityCompromisedException.getMessage());
    }

    /**
     * This method is used to transform any unexpected exception. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param applicationInternalServerErrorException Any unexpected exception that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 500 BAD REQUEST
     */
    @ExceptionHandler(value = {ApplicationInternalServerErrorException.class})
    public ResponseEntity<?> handleUnexpectedException(ApplicationInternalServerErrorException applicationInternalServerErrorException) {
        return ResponseEntity.internalServerError()
                .contentType(MediaType.TEXT_PLAIN)
                .body(applicationInternalServerErrorException.getMessage());
    }

    /**
     * This method is used to transform any ApplicationOptimisticLockException or exception that extend it, which
     * could be thrown while editing the object, a parallel editing action occurred. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param applicationOptimisticLockException Any unexpected exception that was caught in order to be transformed to HTTP Response.
     *
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 409 CONFLICT
     */
    @ExceptionHandler(value = {ApplicationOptimisticLockException.class})
    public ResponseEntity<?> handleApplicationOptimisticLockException(ApplicationOptimisticLockException applicationOptimisticLockException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .body(applicationOptimisticLockException.getMessage());
    }
}
