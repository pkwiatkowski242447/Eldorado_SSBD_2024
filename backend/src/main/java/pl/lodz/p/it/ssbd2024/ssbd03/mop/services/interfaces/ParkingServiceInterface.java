package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.UUID;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingServiceInterface {

    /**
     * Retrieves from the database sector by id.
     *
     * @param id Sector's id.
     * @return If Sector with the given id was found returns Sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Sector getSectorById(UUID id) throws ApplicationBaseException;

    /**
     * Removes parking from the database by its id.
     *
     * @param id Identifier of the parking to be removed.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void removeParkingById(UUID id) throws ApplicationBaseException;
}
