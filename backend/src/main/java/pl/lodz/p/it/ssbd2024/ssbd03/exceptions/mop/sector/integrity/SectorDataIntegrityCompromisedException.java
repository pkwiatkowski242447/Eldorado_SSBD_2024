package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.integrity;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorDataIntegrityCompromisedException extends SectorBaseException {

    public SectorDataIntegrityCompromisedException() {
        super(I18n.DATA_INTEGRITY_COMPROMISED);
    }

    public SectorDataIntegrityCompromisedException(String message) {
        super(message);
    }
}
