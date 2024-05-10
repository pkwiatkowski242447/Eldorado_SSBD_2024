package pl.lodz.p.it.ssbd2024.ssbd03.exceptions;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ApplicationOptimisticLockException extends ApplicationBaseException {
    public ApplicationOptimisticLockException() {
        super(I18n.OPTIMISTIC_LOCK_EXCEPTION);
    }
}
