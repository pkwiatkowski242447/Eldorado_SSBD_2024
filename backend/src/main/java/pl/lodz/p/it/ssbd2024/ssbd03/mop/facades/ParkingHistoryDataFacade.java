package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository used to manage historical Parking Entities.
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class ParkingHistoryDataFacade extends AbstractFacade<ParkingHistoryData> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public ParkingHistoryDataFacade() {
        super(ParkingHistoryData.class);
    }

    /**
     * Retrieves an entity manager.
     *
     * @return Entity manager associated with the facade.
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    // C - create methods

    /**
     * Persists a new ParkingHistoryData in the database.
     *
     * @param parkingHistoryData Entity to be persisted.
     */
    @Override
    @RolesAllowed({Authorities.EDIT_PARKING})
    public void create(ParkingHistoryData parkingHistoryData) throws ApplicationBaseException {
        TypedQuery<Integer> findParkingByIdQuery = entityManager.createNamedQuery("ParkingHistoryData.checkIfEntityExists", Integer.class);
        findParkingByIdQuery.setParameter("id", parkingHistoryData.getId());
        findParkingByIdQuery.setParameter("version", parkingHistoryData.getVersion());
        boolean exists = !findParkingByIdQuery.getResultList().isEmpty();

        if (!exists) super.create(parkingHistoryData);
    }

    // R - read methods

    /**
     * This method is used to find parking history versions by parking id.
     *
     * @param id         ID of the searched parking.
     * @param pageNumber Number of the page.
     * @param pageSize   Size of the page.
     * @return If there are historic entries for the requested parking, this method returns part of the entries
     * specified by pageSize and pageNumber. Otherwise, empty list.
     */
    @RolesAllowed(Authorities.GET_ACCOUNT_HISTORICAL_DATA)
    public List<ParkingHistoryData> findByParkingId(UUID id, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<ParkingHistoryData> findParkingByIdQuery = entityManager.createNamedQuery("ParkingHistoryData.findByParkingId", ParkingHistoryData.class);
            findParkingByIdQuery.setParameter("id", id);
            findParkingByIdQuery.setFirstResult(pageNumber * pageSize);
            findParkingByIdQuery.setMaxResults(pageSize);
            List<ParkingHistoryData> list = findParkingByIdQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }
}
