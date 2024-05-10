package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;

/**
 * General exception handling component in a form of @ControllerAdvice for exceptions that are shared
 * between all components.
 */
@ControllerAdvice
public class GenericExceptionResolver {

    /**
     * This method is used to transform ApplicationOptimisticLockException or its subclasses into HTTP Response with status
     * code 400 BAD REQUEST.
     *
     * @param optimisticLockException Exception that is either ApplicationOptimisticLockException or its subclass, that is
     *                                being caught while propagating from controller.
     *
     * @return 400 BAD REQUEST is returned when instance of ApplicationOptimisticLockException or its subclasses are
     * propagated from controller component.
     */
    @ExceptionHandler(value = { ApplicationOptimisticLockException.class })
    public ResponseEntity<?> handleOptimisticLockException(ApplicationOptimisticLockException optimisticLockException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(optimisticLockException.getMessage());
    }
}
