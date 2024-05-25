package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

/**
 * Data Transfer Object used in changing email address of an account.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEmailDTO {
    @Email(message = AccountMessages.EMAIL_CONSTRAINT_NOT_MET)
    @Size(min = AccountsConsts.EMAIL_MIN_LENGTH, message = AccountMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountsConsts.EMAIL_MAX_LENGTH, message = AccountMessages.EMAIL_TOO_LONG)
    private String email;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountEmailDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
