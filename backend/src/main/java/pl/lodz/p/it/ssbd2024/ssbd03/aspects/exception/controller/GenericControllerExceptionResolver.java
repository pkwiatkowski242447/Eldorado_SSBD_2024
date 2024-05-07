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

    @ExceptionHandler(value = { AccountConstraintViolationException.class })
    public ResponseEntity<?> handleAccountConstraintViolationException(AccountConstraintViolationException accountConstraintViolationException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountConstraintViolationExceptionDTO(accountConstraintViolationException));
    }

    @ExceptionHandler(value = { AccountConflictException.class })
    public ResponseEntity<?> handleAccountConflictException(AccountConflictException accountConflictException) {
        return ResponseEntity.badRequest()
               .contentType(MediaType.APPLICATION_JSON)
               .body(accountConflictException.getMessage());
    }
}
