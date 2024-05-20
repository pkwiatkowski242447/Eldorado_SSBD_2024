package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to use Token object (with invalid token value - due to
 * its TTL).
 * @see Token
 */
public class TokenNotValidException extends TokenBaseException {
    public TokenNotValidException() {
        super(I18n.TOKEN_NOT_VALID_EXCEPTION);
    }

    public TokenNotValidException(String message) {
        super(message);
    }

    public TokenNotValidException(Throwable cause) {
        super(I18n.TOKEN_NOT_VALID_EXCEPTION, cause);
    }

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
