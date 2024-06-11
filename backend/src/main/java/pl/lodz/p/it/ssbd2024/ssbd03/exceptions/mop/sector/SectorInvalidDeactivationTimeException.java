package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 *
 */
public class SectorInvalidDeactivationTimeException extends SectorBaseException {

    public SectorInvalidDeactivationTimeException() {
        super(I18n.SECTOR_DEACTIVATION_INVALID_TIME);
    }

    public SectorInvalidDeactivationTimeException(Throwable cause) {
        super(I18n.SECTOR_DEACTIVATION_INVALID_TIME, cause);
    }
}
