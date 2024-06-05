package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.validation;

import lombok.Getter;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ParkingConstraintViolationException extends ParkingValidationException {
    private Set<String> violations = new HashSet<>();

    public ParkingConstraintViolationException(Set<String> violations) {
        super(I18n.PARKING_CONSTRAINT_VIOLATION);
        this.violations = violations;
    }

    public ParkingConstraintViolationException(Throwable cause, Set<String> violations) {
        super(I18n.ACCOUNT_CONSTRAINT_VIOLATION, cause);
        this.violations = violations;
    }
}
