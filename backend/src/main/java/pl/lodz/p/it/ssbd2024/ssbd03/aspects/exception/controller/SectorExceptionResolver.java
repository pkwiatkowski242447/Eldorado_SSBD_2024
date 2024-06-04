package pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.sector.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.sector.SectorNotFoundException;

@ControllerAdvice
@Order(1)
public class SectorExceptionResolver {
    /**
     * This method is used to transform SectorNotFoundException,that is being propagated from controller component
     * into HTTP response, so that exception is handled without container intervention.
     *
     * @param sectorNotFoundException Exception that will be processed into HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {SectorNotFoundException.class})
    public ResponseEntity<?> handleSectorNotFoundException(SectorNotFoundException sectorNotFoundException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(sectorNotFoundException));
    }

    /**
     * This method is used to transform SectorNotFoundException,that is being propagated from controller component
     * into HTTP response, so that exception is handled without container intervention.
     *
     * @param sectorAlreadyActiveException Exception that will be processed into HTTP Response.
     * @return When specified exception is propagated from controller component this method will catch it and transform
     * to HTTP Response with status code 400 BAD REQUEST
     */
    @ExceptionHandler(value = {SectorAlreadyActiveException.class})
    public ResponseEntity<?> handleSectorAlreadyActiveException(SectorAlreadyActiveException sectorAlreadyActiveException) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDTO(sectorAlreadyActiveException));
    }

}
