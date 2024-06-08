package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class TokenValueAlreadyTakenException extends ApplicationBaseException {
    public TokenValueAlreadyTakenException() {
        super(I18n.TOKEN_VALUE_ALREADY_TAKEN);
    }
}
