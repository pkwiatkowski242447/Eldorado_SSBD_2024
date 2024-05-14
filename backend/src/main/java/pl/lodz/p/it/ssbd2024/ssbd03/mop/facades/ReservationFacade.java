package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Implementation of AbstractFacade that provides CRUD operations
 * on the Reservation class using the injected EntityManager implementation.
 * @see Reservation
 * @see jakarta.persistence.EntityManager
 */
@Repository
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Invokes superclass constructor with Class object passed.
     */
    public ReservationFacade() {
        super(Reservation.class);
    }

    /**
     * Returns injected EntityManager implementation.
     * @return EntityManager implementation
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Invokes superclass create method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Transactional
    @Override
    public void create(Reservation entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Invokes superclass edit method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Transactional
    @Override
    public void edit(Reservation entity) {
        super.edit(entity);
    }

    /**
     * Invokes superclass remove method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Transactional
    @Override
    public void remove(Reservation entity) {
        super.remove(entity);
    }

    /**
     * Invokes superclass find method by passed entity id.
     * @param id The UUID type identifier of the entity being searched for
     * @return Entity, wrapped in Optional class, with identifiers equals to id param
     */
    @Transactional
    @Override
    public Optional<Reservation> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Invokes superclass find with refreshing method by passed entity id.
     * @param id The UUID type identifier of the entity being searched for
     * @return Entity, wrapped in Optional class, with identifiers equals to id param
     */
    @Transactional
    @Override
    public Optional<Reservation> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * Invokes superclass find all method.
     * @return All Reservation entities
     */
    @Transactional
    @Override
    public List<Reservation> findAll() {
        return super.findAll();
    }

    /**
     * Returns all Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All Reservation entities from selected page
     */
    @Transactional
    public List<Reservation> findAllWithPagination(int page, int pageSize) {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findAll", Reservation.class)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all active Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All active Reservation entities from selected page
     */
    @Transactional
    public List<Reservation> findActiveReservationsWithPagination(UUID clientId, int page, int pageSize) {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findActiveReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all historical Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All historical Reservation entities from selected page
     */
    @Transactional
    public List<Reservation> findHistoricalReservationsWithPagination(UUID clientId, int page, int pageSize) {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findHistoricalReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all Reservation entities for the selected Sector, based on NamedQuery defined on Reservation class,
     * with pagination.
     * @param sectorId The UUID type identifier of the selected Sector
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All Reservation entities for selected Sector and page
     */
    @Transactional
    public List<Reservation> findSectorReservationsWithPagination(UUID sectorId, int page, int pageSize) {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findSectorReservations", Reservation.class)
                .setParameter("sectorId", sectorId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Invokes superclass count method.
     * @return Returns the total number of Reservation entities
     */
    @Transactional
    @Override
    public int count() {
        return super.count();
    }
}
