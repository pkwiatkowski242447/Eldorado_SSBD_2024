package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationAccessDeniedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.mapper.MapperBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * General exception handling component in a form of @ControllerAdvice for exceptions that are shared
 * between all components.
 */
@ControllerAdvice
@Order(2)
public class GenericExceptionResolver {

    /**
     * This method is used to transform ApplicationOptimisticLockException or its subclasses into HTTP Response with status
     * code 400 BAD REQUEST.
     *
     * @param optimisticLockException Exception that is either ApplicationOptimisticLockException or its subclass, that is
     *                                being caught while propagating from controller.
     * @return 400 BAD REQUEST is returned when instance of ApplicationOptimisticLockException or its subclasses are
     * propagated from controller component.
     */
    @ExceptionHandler(value = {ApplicationOptimisticLockException.class})
    public ResponseEntity<?> handleOptimisticLockException(ApplicationOptimisticLockException optimisticLockException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(optimisticLockException));
    }

    /**
     * This method is used to transform any InvalidRequestHeaderException or exception that extend it.
     * After such exception is propagated from controller it will be caught and transformed into HTTP Response.
     *
     * @param invalidRequestHeaderException InvalidRequestHeaderException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {InvalidRequestHeaderException.class})
    public ResponseEntity<?> handleInvalidRequestHeaderException(InvalidRequestHeaderException invalidRequestHeaderException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(invalidRequestHeaderException));
    }

    /**
     * This method is used to transform any unexpected exception. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param applicationInternalServerErrorException Any unexpected exception that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 500 BAD REQUEST
     */
    @ExceptionHandler(value = {ApplicationInternalServerErrorException.class})
    public ResponseEntity<?> handleUnexpectedException(ApplicationInternalServerErrorException applicationInternalServerErrorException) {
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(applicationInternalServerErrorException));
    }

    /**
     * This method is used to transform any MapperBaseException or exception that extend it, which could be thrown when
     * trying to map not handled / invalid. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param mapperBaseException MapperBaseException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {MapperBaseException.class})
    public ResponseEntity<?> handleMapperBaseException(MapperBaseException mapperBaseException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(mapperBaseException));
    }

    /**
     * This method is used to transform any ApplicationDatabaseException or exception that extend it,
     * which wraps unexpected database exceptions. After such exception is propagated from controller
     * it will be caught and transformed into HTTP Response.
     *
     * @param applicationDatabaseException ApplicationDatabaseException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(value = {ApplicationDatabaseException.class})
    public ResponseEntity<?> handleApplicationDatabaseException(ApplicationDatabaseException applicationDatabaseException) {
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(applicationDatabaseException));
    }

    /**
     * This method is used to transform any InvalidDataFormatException or exception that extend it, which could be thrown when
     * passed invalid data. After such exception is propagated from controller it will be caught and transformed into HTTP Response.
     *
     * @param invalidDataFormatException InvalidDataFormatException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {InvalidDataFormatException.class})
    public ResponseEntity<?> handleInvalidDataFormatException(InvalidDataFormatException invalidDataFormatException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(invalidDataFormatException));
    }

    /**
     * This method is used to transform any IllegalOperationException or exception that extend it, which could be thrown when
     * trying to perform a prohibited operation. After such exception is propagated from controller it will be caught
     * and transformed into HTTP Response.
     *
     * @param illegalOperationException IllegalOperationException that was caught in order to be transformed to HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {IllegalOperationException.class})
    public ResponseEntity<?> handleIllegalOperationException(IllegalOperationException illegalOperationException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(illegalOperationException));
    }

    /**
     * This method is used to transform any Access Denied exception (either in form of ApplicationAccessDeniedException -
     * exception thrown when calling principal does not have specified role in service and facade layers) or AccessDeniedException
     * thrown in the controllers layer.
     *
     * @param ex        The exception, regarding lack of authorities to perform certain actions, to be transformed to HTTP Response.
     * @return 401 UNAUTHORIZED response, indicating that the user is not authorized to perform certain actions in the
     * application.
     */
    @ExceptionHandler(value = {AccessDeniedException.class, ApplicationAccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(I18n.ACCESS_DENIED_EXCEPTION));
    }
}
