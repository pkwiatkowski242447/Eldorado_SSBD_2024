package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.integrity;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.ParkingBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorDataIntegrityCompromisedException extends ParkingBaseException {
    public SectorDataIntegrityCompromisedException() {
        super(I18n.DATA_INTEGRITY_COMPROMISED);
    }

    public SectorDataIntegrityCompromisedException(String message) {
        super(message);
    }
}
