package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.sector;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorNotFoundException extends ApplicationBaseException {
    public SectorNotFoundException() {
        super(I18n.SECTOR_NOT_FOUND);
    }
}
