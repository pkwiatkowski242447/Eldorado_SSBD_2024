package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDTO {

    private String message;
    private Set<String> violations = new HashSet<>();

    public ExceptionDTO(Throwable exception) {
        this.message = exception.getMessage();

        if (exception instanceof AccountConstraintViolationException accountConstraintViolationException) {
            this.violations = accountConstraintViolationException.getViolations();
        }
    }

    public ExceptionDTO(String message) {
        this.message = message;
    }
}
