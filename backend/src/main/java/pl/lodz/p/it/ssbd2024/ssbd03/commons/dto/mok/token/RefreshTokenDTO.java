package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations.IsJsonWebToken;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DTOConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.DTOMessages;

/**
 * Data transfer object containing only refresh token,
 * used for refreshing user session in the application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@LoggerInterceptor
public class RefreshTokenDTO {

    @NotBlank(message = DTOMessages.RT_REFRESH_TOKEN_BLANK)
    @Size(min = DTOConsts.REFRESH_TOKEN_MIN_LENGTH, message = DTOMessages.RT_REFRESH_TOKEN_TOO_SHORT)
    @Size(max = DTOConsts.REFRESH_TOKEN_MAX_LENGTH, message = DTOMessages.RT_REFRESH_TOKEN_TOO_LONG)
    @IsJsonWebToken(message = DTOMessages.RT_REFRESH_TOKEN_NOT_JWT)
    private String refreshToken;

    /**
     * Custom toString() method implementation that does not return any information relating
     * to the business data.
     *
     * @return String representation of the RefreshTokenDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
