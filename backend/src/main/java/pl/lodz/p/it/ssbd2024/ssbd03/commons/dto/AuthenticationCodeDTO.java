package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
