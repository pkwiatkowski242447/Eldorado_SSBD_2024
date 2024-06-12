package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@LoggerInterceptor
public class ApplicationConstraintViolationDTO extends ExceptionDTO {

    private Set<String> violations;

    public ApplicationConstraintViolationDTO(Set<String> argumentViolations) {
        super(I18n.INVALID_ARGUMENT_EXCEPTION);
        this.violations = argumentViolations;
    }
}
