package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to authenticate.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginDTO {

    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Schema(description = "Secret string of characters used to authenticate to the application", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
