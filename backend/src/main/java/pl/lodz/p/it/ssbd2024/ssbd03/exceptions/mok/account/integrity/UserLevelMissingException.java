package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class UserLevelMissingException extends AccountDataIntegrityCompromisedException {
    public UserLevelMissingException() {
        super(I18n.USER_LEVEL_MISSING);
    }
}
