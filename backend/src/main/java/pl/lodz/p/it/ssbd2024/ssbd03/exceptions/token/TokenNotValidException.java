package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token;

/**
 * Used to specify an Exception related to use Token object (with invalid token value - due to
 * its TTL).
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.Token
 */
public class TokenNotValidException extends TokenBaseException {
    public TokenNotValidException(String message) {
        super(message);
    }

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
