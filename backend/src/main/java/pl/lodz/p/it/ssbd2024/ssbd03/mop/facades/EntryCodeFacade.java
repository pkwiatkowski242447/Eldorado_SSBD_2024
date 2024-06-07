package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.EntryCode;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage entry code entities in the database.
 *
 * @see Account
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class EntryCodeFacade extends AbstractFacade<EntryCode> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public EntryCodeFacade() {
        super(EntryCode.class);
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

    @Override
    @RolesAllowed({Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION})
    public void create(EntryCode entity) throws ApplicationBaseException {
        super.create(entity);
    }

    @Override
    @DenyAll
    public void edit(EntryCode entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Authorities.EXIT_PARKING})
    public void remove(EntryCode entity) throws ApplicationBaseException {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.EXIT_PARKING
    })
    public Optional<EntryCode> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    @Override
    @DenyAll
    public Optional<EntryCode> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    @Override
    @DenyAll
    public List<EntryCode> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    @Override
    @DenyAll
    public void refreshAll(List<EntryCode> list) throws ApplicationBaseException {
        super.refreshAll(list);
    }

    @Override
    @DenyAll
    public int count() throws ApplicationBaseException {
        return super.count();
    }

    @RolesAllowed({
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.EXIT_PARKING
    })
    public Optional<EntryCode> findEntryCodeByReservationId(UUID reservationId) throws ApplicationBaseException {
        TypedQuery<EntryCode> findByReservationId = this.getEntityManager().createNamedQuery("EntryCode.findEntryCodeByReservationId", EntryCode.class);
        findByReservationId.setParameter("reservationId", reservationId);
        EntryCode entryCode = findByReservationId.getSingleResult();
        this.getEntityManager().refresh(entryCode);
        return Optional.of(entryCode);
    }
}
