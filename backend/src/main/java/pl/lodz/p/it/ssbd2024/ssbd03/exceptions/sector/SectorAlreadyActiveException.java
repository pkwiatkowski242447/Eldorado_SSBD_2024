package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.sector;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorAlreadyActiveException extends ApplicationBaseException {
    public SectorAlreadyActiveException() {
        super(I18n.SECTOR_ALREADY_ACTIVE);
    }
}
