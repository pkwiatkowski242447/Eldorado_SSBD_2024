package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.edit;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class SectorEditOfTypeOrMaxPlacesWhenActiveException extends SectorBaseException {

    public SectorEditOfTypeOrMaxPlacesWhenActiveException() {
        super(I18n.SECTOR_EDIT_OF_TYPE_OR_MAX_PLACES_WHEN_ACTIVE);
    }
    
}
