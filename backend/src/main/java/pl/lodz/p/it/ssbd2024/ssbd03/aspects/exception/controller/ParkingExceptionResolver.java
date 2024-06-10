package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.exception.ParkingConstraintViolationExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict.ParkingDeleteException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.integrity.ParkingDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.validation.ParkingConstraintViolationException;

/**
 * General exception handling component in a form of @ControllerAdvice for Parking exceptions.
 */
@ControllerAdvice
@Order(1)
public class ParkingExceptionResolver {

    /**
     * This method transforms any ParkingNotFoundException, propagating from the controller layer
     * to the HTTP response containing internationalization key with status code, which in this case is
     * 400 BAD REQUEST.
     *
     * @param exception Exception of type ParkingNotFound (or subclass of that exception), propagating from
     *                  the controller layer.
     * @return HTTP Response with status 400 BAD REQUEST and internationalization key, which is located in the
     * Response body.
     */
    @ExceptionHandler(value = {ParkingNotFoundException.class})
    public ResponseEntity<?> handleParkingNotFoundException(ParkingNotFoundException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(exception.getMessage()));
    }

    /**
     * This method transforms any ParkingDeleteException, propagating from the controller layer
     * to the HTTP response containing internationalization key with status code, which in this case is
     * 400 BAD REQUEST.
     *
     * @param exception Exception of type ParkingDeleteException (or subclass of that exception), propagating from
     *                  the controller layer.
     * @return HTTP Response with status 400 BAD REQUEST and internationalization key, which is located in the
     * Response body.
     */
    @ExceptionHandler(value = {ParkingDeleteException.class})
    public ResponseEntity<?> handleParkingDeleteException(ParkingDeleteException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(exception.getMessage()));
    }

    /**
     * This method transforms any ParkingDataIntegrityCompromisedException, propagating from the controller layer
     * to the HTTP response containing internationalization key with status code, which in this case is
     * 409 CONFLICT.
     *
     * @param exception Exception of type ParkingDataIntegrityCompromisedException (or subclass of that exception),
     *                  propagating from the controller layer.
     * @return HTTP Response with status 409 CONFLICT and internationalization key, which is located in the
     * Response body.
     */
    @ExceptionHandler(value = {ParkingDataIntegrityCompromisedException.class})
    public ResponseEntity<?> handleParkingDataIntegrityCompromisedException(ParkingDataIntegrityCompromisedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(exception.getMessage()));
    }

    /**
     * This method transforms any ParkingConstraintViolationException, propagating from the controller layer
     * to the HTTP response containing internationalization key with status code, which in this case is
     * 400 BAD REQUEST.
     *
     * @param exception Exception of type ParkingConstraintViolationException (or subclass of that exception),
     *                  propagating from the controller layer.
     * @return HTTP Response with status 400 BAD REQUEST and internationalization key, which is located in the
     * Response body.
     */
    @ExceptionHandler(value = {ParkingConstraintViolationException.class})
    public ResponseEntity<?> handleParkingConstraintViolationException(ParkingConstraintViolationException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ParkingConstraintViolationExceptionDTO(exception));
    }
}
