package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.read;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.TokenBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related trying to access a Token that doesn't exist.
 * @see Token
 */
public class TokenNotFoundException extends TokenBaseException {

    public TokenNotFoundException() {
        super(I18n.TOKEN_NOT_FOUND_EXCEPTION);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
