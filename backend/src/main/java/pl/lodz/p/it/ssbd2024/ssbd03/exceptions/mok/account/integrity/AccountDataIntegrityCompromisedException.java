package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.AccountBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with trying to modify signature-protected Account properties.
 * @see AccountSignableDTO
 */
public class AccountDataIntegrityCompromisedException extends AccountBaseException {
    public AccountDataIntegrityCompromisedException() {
        super(I18n.DATA_INTEGRITY_COMPROMISED);
    }

    public AccountDataIntegrityCompromisedException(String message) {
        super(message);
    }
}
