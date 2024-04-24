package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ReservationMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an allocation of a parking spot on the Parking.
 * It's used to represent both pre-planned reservations and entries to the Parking without a previously made reservation.
 * In case of a pre-planned reservation it's possible to have multiple ParkingEvents.
 * If an anonymous user enters the Parking there is no information related to the Client set.
 * @see ParkingEvent
 * @see Sector
 * @see Client
 */
@Entity
@Table(name = DatabaseConsts.RESERVATION_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor

@NamedQueries({
        @NamedQuery(
                name = "Reservation.findAll",
                query = """
                        SELECT r FROM Reservation r
                        ORDER BY r.beginTime"""
        ),
        @NamedQuery(
                name = "Reservation.findActiveReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.client.id = :clientId
                          AND (r.endTime IS NULL OR CURRENT_TIMESTAMP < r.endTime)
                        ORDER BY r.beginTime"""
        ),
        @NamedQuery(
                name = "Reservation.findHistoricalReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.client.id = :clientId
                          AND r.endTime IS NOT NULL AND CURRENT_TIMESTAMP >= r.endTime
                        ORDER BY r.beginTime"""
        ),
        @NamedQuery(
                name = "Reservation.findSectorReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.sector.id = :sectorId
                        ORDER BY r.beginTime"""
        )
})
public class Reservation extends AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = DatabaseConsts.RESERVATION_CLIENT_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, updatable = false)
    @Getter
    private Client client;

    @NotNull(message = ReservationMessages.SECTOR_NULL)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.RESERVATION_SECTOR_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    private Sector sector;

    @Column(name = DatabaseConsts.RESERVATION_BEGIN_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter
    private LocalDateTime beginTime;

    @Column(name = DatabaseConsts.RESERVATION_END_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter
    private LocalDateTime endTime;

    @NotNull(message = ReservationMessages.LIST_OF_PARKING_EVENTS_NULL)
    @OneToMany(mappedBy = DatabaseConsts.RESERVATION_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @Getter
    private final List<ParkingEvent> parkingEvents = new ArrayList<>();

    /**
     * Constructs a new reservation for a non-anonymous client.
     * @param client The Client on behalf of whom the reservation is made.
     * @param sector Sector in which the parking spot in allocated.
     */
    public Reservation(Client client, Sector sector) {
        this.client = client;
        this.sector = sector;
    }

    /**
     * Constructs a new reservation for a non-anonymous client.
     * @param sector Sector in which the parking spot in allocated.
     */
    public Reservation(Sector sector) {
        this(null, sector);
    }
}
