package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DTOConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.DTOMessages;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class AccountPasswordDTO {

    @NotBlank(message = DTOMessages.PASSWORD_BLANK)
    @Pattern(regexp = DTOConsts.PASSWORD_REGEX, message = DTOMessages.PASSWORD_REGEX_NOT_MET)
    @Size(min = DTOConsts.PASSWORD_MIN_LENGTH, message = DTOMessages.PASSWORD_TOO_SHORT)
    @Size(max = DTOConsts.PASSWORD_MAX_LENGTH, message = DTOMessages.PASSWORD_TOO_LONG)
    private String password;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountPasswordDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
