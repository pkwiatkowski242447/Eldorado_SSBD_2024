package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ExceptionDTO {

    private String message;

    public ExceptionDTO(Throwable exception) {
        this.message = exception.getMessage();
    }

    public ExceptionDTO(String message) {
        this.message = message;
    }
}

