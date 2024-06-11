package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.validation.ParkingConstraintViolationException;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class ParkingConstraintViolationExceptionDTO extends ExceptionDTO {

    private Set<String> violations;

    public ParkingConstraintViolationExceptionDTO(ParkingConstraintViolationException exception) {
        super(exception.getMessage());
        this.violations = exception.getViolations();
    }
}
