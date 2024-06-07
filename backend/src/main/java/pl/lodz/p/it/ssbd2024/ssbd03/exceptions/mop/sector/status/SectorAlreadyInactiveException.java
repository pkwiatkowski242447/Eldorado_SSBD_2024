package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorAlreadyInactiveException extends SectorBaseException {

    public SectorAlreadyInactiveException() {
        super(I18n.SECTOR_ALREADY_INACTIVE);
    }

}
