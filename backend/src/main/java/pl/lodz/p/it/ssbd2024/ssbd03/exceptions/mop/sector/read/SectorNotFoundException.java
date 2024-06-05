package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorNotFoundException extends SectorBaseException {

    public SectorNotFoundException() {
        super(I18n.SECTOR_NOT_FOUND);
    }
}
