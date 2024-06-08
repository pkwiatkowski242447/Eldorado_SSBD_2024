package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorAlreadyActiveException extends SectorBaseException {

    public SectorAlreadyActiveException() {
        super(I18n.SECTOR_ALREADY_ACTIVE);
    }
}
