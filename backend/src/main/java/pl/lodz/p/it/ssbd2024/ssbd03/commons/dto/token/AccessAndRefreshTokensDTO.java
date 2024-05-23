package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data transfer object, used for sending JWT access token and refresh token
 * to the client, after user is successfully authenticated in the application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessAndRefreshTokensDTO {

    private String accessToken;
    private String refreshToken;

    /**
     * Custom toString() method implementation that does not return any information
     * relating to the business data.
     *
     * @return String representation of the AccessAndRefreshTokensDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
