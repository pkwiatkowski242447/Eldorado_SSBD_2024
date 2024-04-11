package pl.lodz.p.it.ssbd2024.ssbd03.mop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

@Repository
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    public ReservationFacade() {
        super(Reservation.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Transactional
    @Override
    public void create(Reservation entity) {
        super.create(entity);
    }

    @Transactional
    @Override
    public void edit(Reservation entity) {
        super.edit(entity);
    }

    @Transactional
    @Override
    public void remove(Reservation entity) {
        super.remove(entity);
    }

    @Transactional
    @Override
    public Optional<Reservation> find(UUID id) {
        return super.find(id);
    }

    @Transactional
    @Override
    public Optional<Reservation> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Transactional
    @Override
    public List<Reservation> findAll() {
        return super.findAll();
    }

    @Transactional
    public List<Reservation> findAllWithPagination(int page, int pageSize) {
        return getEntityManager()
                .createNamedQuery("Reservation.findAll", Reservation.class)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Reservation> findActiveReservationsWithPagination(UUID clientId, int page, int pageSize) {
        return getEntityManager()
                .createNamedQuery("Reservation.findActiveReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Reservation> findHistoricalReservationsWithPagination(UUID clientId, int page, int pageSize) {
        return getEntityManager()
                .createNamedQuery("Reservation.findHistoricalReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    @Override
    public int count() {
        return super.count();
    }
}
