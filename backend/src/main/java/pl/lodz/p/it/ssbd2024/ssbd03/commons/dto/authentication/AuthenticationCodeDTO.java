package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object used for the second step in multifactor
 * authentication, when code is passed to authenticate user.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationCodeDTO {
    private String userLogin;
    private String authCodeValue;
    private String language;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AuthenticationCodeDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Login: ", userLogin)
                .toString();
    }
}
