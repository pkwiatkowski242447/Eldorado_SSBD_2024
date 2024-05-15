package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class TokenDataExtractionException extends TokenBaseException {

    public TokenDataExtractionException() {
        super(I18n.TOKEN_DATA_EXTRACTION_EXCEPTION);
    }

    public TokenDataExtractionException(Throwable cause) {
        super(I18n.TOKEN_DATA_EXTRACTION_EXCEPTION, cause);
    }
}
