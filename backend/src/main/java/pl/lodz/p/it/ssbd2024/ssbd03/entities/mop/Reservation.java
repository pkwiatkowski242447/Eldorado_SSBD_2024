package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
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
@Table(
        name = DatabaseConsts.RESERVATION_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.RESERVATION_CLIENT_ID_INDEX, columnList = DatabaseConsts.RESERVATION_CLIENT_ID_COLUMN),
                @Index(name = DatabaseConsts.RESERVATION_SECTOR_ID_INDEX, columnList = DatabaseConsts.RESERVATION_SECTOR_ID_COLUMN)
        }
)
@LoggerInterceptor
@NoArgsConstructor
@Getter
@NamedQueries({
        @NamedQuery(
                name = "Reservation.findAll",
                query = """
                        SELECT r FROM Reservation r
                        GROUP BY r.id
                        ORDER BY r.beginTime
                        """
        ),
        // Client reservations
        @NamedQuery(
                name = "Reservation.findActiveReservationsByLogin",
                query = """
                       SELECT r FROM Reservation r
                        WHERE r.client.account.login = :clientLogin
                          AND r.status IN (ReservationStatus.AWAITING,
                                           ReservationStatus.IN_PROGRESS)
                        GROUP BY r.id
                        ORDER BY r.beginTime
                       """
        ),
        @NamedQuery(
                name = "Reservation.findHistoricalReservationsByLogin",
                query = """
                       SELECT r FROM Reservation r
                        WHERE r.client.account.login = :clientLogin
                          AND r.status IN (ReservationStatus.COMPLETED_MANUALLY,
                                           ReservationStatus.COMPLETED_AUTOMATICALLY,
                                           ReservationStatus.CANCELLED,
                                           ReservationStatus.TERMINATED)
                        GROUP BY r.id
                        ORDER BY r.beginTime
                       """
        ),
        // Get sector
        @NamedQuery(
                name = "Reservation.findSectorReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.sector.id = :sectorId
                        ORDER BY r.beginTime
                        """
        ),
        // Schedule queries
        @NamedQuery(
                name = "Reservation.findAllReservationsMarkedForTermination",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.beginTime < :timestamp
                        AND r.status = ReservationStatus.IN_PROGRESS
                        AND MOD(SIZE(r.parkingEvents), 2) = 1
                        ORDER BY r.beginTime ASC
                        """
        ),
        @NamedQuery(
                name = "Reservation.findAllReservationsMarkedForCompleting",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.endTime < CURRENT_TIMESTAMP
                        AND (r.status = ReservationStatus.AWAITING
                        OR r.status = ReservationStatus.IN_PROGRESS)
                        AND MOD(SIZE(r.parkingEvents), 2) = 0
                        ORDER BY r.beginTime ASC
                        """
        ),
        // Find all parking events for reservation
        @NamedQuery(
                name = "Reservation.findAllParkingEventsForGivenReservation",
                query = """
                        SELECT DISTINCT p FROM ParkingEvent p
                        WHERE p.reservation.id = :reservationId
                        ORDER BY p.createdBy DESC
                        """
        ),
        // Deactivating sector
        @NamedQuery(
                name = "Reservation.findAllReservationsToCancelBeforeDeactivation",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.sector.id = :sectorId
                            AND r.beginTime > :timestamp
                        ORDER BY r.beginTime ASC
                        """
        ),
        @NamedQuery(
                name = "Reservation.findClientReservation",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.id = :reservationId
                            AND r.client.account.login = :ownerLogin
                        """
        ),
        // Creating reservation
        @NamedQuery(
                name = "Reservation.countAllActiveUserReservationByLogin",
                query = """
                        SELECT COUNT(*) FROM Reservation r
                        WHERE r.client.account.login = :clientLogin
                          AND r.status IN (ReservationStatus.AWAITING,
                                           ReservationStatus.IN_PROGRESS)
                        """
        ),
        @NamedQuery(
                name = "Reservation.countAllSectorReservationInTimeframe",
                query = """
                        SELECT COUNT(*) FROM Reservation r
                        WHERE r.sector.id = :sectorId
                        AND
                        r.status != ReservationStatus.CANCELLED
                        AND
                        (
                            (
                                (r.beginTime BETWEEN :beginTimeMinusMaxReservationTime AND :beginTimePlusMaxReservationTime)
                                AND
                                (
                                    (
                                        r.endTime IS NULL
                                        OR
                                        (
                                            r.endTime < :beginTime
                                            AND
                                            (
                                                :current_timestamp < r.endTime
                                                OR
                                                MOD(SIZE(r.parkingEvents), 2) = 1
                                            )
                                        )
                                    )
                                    OR
                                    r.endTime > :beginTime
                                )
                            )
                            OR
                            (r.beginTime BETWEEN :beginTime AND :beginTimePlusMaxReservationTime)
                        )
                        """
        ),
        @NamedQuery(
                name = "Reservation.getAvailablePremiumSectorsNow",
                query = """
                        SELECT sectorFin FROM Sector sectorFin
                        WHERE (sectorFin.deactivationTime IS NULL OR sectorFin.deactivationTime > :deactivationMinimum)
                        AND sectorFin.parking.id = :parkingId
                        AND sectorFin NOT IN (
                            SELECT r.sector FROM Reservation r
                            WHERE (r.sector.deactivationTime IS NULL OR r.sector.deactivationTime > :deactivationMinimum)
                            AND r.status IN (ReservationStatus.AWAITING, ReservationStatus.IN_PROGRESS)
                            AND r.sector.parking.id = :parkingId
                            AND
                                (
                                    (
                                        (r.beginTime BETWEEN :currentTimeMinusReserve AND :currentTimePlusReserve)
                                        AND
                                        (
                                            (
                                                r.endTime IS NULL
                                                OR
                                                (
                                                    r.endTime < :currentTime
                                                    AND
                                                    MOD(SIZE(r.parkingEvents), 2) = 1
                                                )
                                            )
                                            OR
                                            r.endTime > :currentTime
                                        )
                                    )
                                    OR
                                    (r.beginTime BETWEEN :currentTime AND :currentTimePlusReserve)
                                )
                            GROUP BY r.sector, r.sector.maxPlaces
                            HAVING COUNT(*) >= r.sector.maxPlaces
                        )
                        """
        ),
        @NamedQuery(
                name = "Reservation.getAvailableStandardSectorsNow",
                query = """
                        SELECT sectorFin FROM Sector sectorFin
                        WHERE (sectorFin.deactivationTime IS NULL OR sectorFin.deactivationTime > :deactivationMinimum)
                        AND sectorFin.type IN (SectorType.UNCOVERED, SectorType.COVERED)
                        AND sectorFin.parking.id = :parkingId
                        AND sectorFin NOT IN (
                            SELECT r.sector FROM Reservation r
                            WHERE (r.sector.deactivationTime IS NULL OR r.sector.deactivationTime > :deactivationMinimum)
                            AND r.status IN (ReservationStatus.AWAITING, ReservationStatus.IN_PROGRESS)
                            AND r.sector.parking.id = :parkingId
                            AND
                                (
                                    (
                                        (r.beginTime BETWEEN :currentTimeMinusReserve AND :currentTimePlusReserve)
                                        AND
                                        (
                                            (
                                                r.endTime IS NULL
                                                OR
                                                (
                                                    r.endTime < :currentTime
                                                    AND
                                                    MOD(SIZE(r.parkingEvents), 2) = 1
                                                )
                                            )
                                            OR
                                            r.endTime > :currentTime
                                        )
                                    )
                                    OR
                                    (r.beginTime BETWEEN :currentTime AND :currentTimePlusReserve)
                                )
                            GROUP BY r.sector, r.sector.maxPlaces
                            HAVING COUNT(*) >= r.sector.maxPlaces
                        )
                        """
        ),
        @NamedQuery(
                name = "Reservation.getAvailableBasicSectorsNow",
                query = """
                        SELECT sectorFin FROM Sector sectorFin
                        WHERE (sectorFin.deactivationTime IS NULL OR sectorFin.deactivationTime > :deactivationMinimum)
                        AND sectorFin.type = SectorType.UNCOVERED
                        AND sectorFin.parking.id = :parkingId
                        AND sectorFin NOT IN (
                            SELECT r.sector FROM Reservation r
                            WHERE (r.sector.deactivationTime IS NULL OR r.sector.deactivationTime > :deactivationMinimum)
                            AND r.status IN (ReservationStatus.AWAITING, ReservationStatus.IN_PROGRESS)
                            AND r.sector.parking.id = :parkingId
                            AND
                                (
                                    (
                                        (r.beginTime BETWEEN :currentTimeMinusReserve AND :currentTimePlusReserve)
                                        AND
                                        (
                                            (
                                                r.endTime IS NULL
                                                OR
                                                (
                                                    r.endTime < :currentTime
                                                    AND
                                                    MOD(SIZE(r.parkingEvents), 2) = 1
                                                )
                                            )
                                            OR
                                            r.endTime > :currentTime
                                        )
                                    )
                                    OR
                                    (r.beginTime BETWEEN :currentTime AND :currentTimePlusReserve)
                                )
                            GROUP BY r.sector, r.sector.maxPlaces
                            HAVING COUNT(*) >= r.sector.maxPlaces
                        )
                        """
        )
})
public class Reservation extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Enum class representing the status of the Reservation entity.
     */
    public enum ReservationStatus { AWAITING, IN_PROGRESS, COMPLETED_MANUALLY, COMPLETED_AUTOMATICALLY, CANCELLED, TERMINATED }

    /**
     * The client associated with this reservation.
     */
    @ManyToOne
    @JoinColumn(
            name = DatabaseConsts.RESERVATION_CLIENT_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.RESERVATION_CLIENT_ID_FK),
            updatable = false
    )
    private Client client;

    /**
     * The sector in which the parking spot is allocated for this reservation.
     */
    @NotNull(message = ReservationMessages.SECTOR_NULL)
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = DatabaseConsts.RESERVATION_SECTOR_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.RESERVATION_SECTOR_ID_FK),
            nullable = false, updatable = false
    )
    private Sector sector;

    /**
     * The beginning time of this reservation.
     */
    @Column(name = DatabaseConsts.RESERVATION_BEGIN_TIME_COLUMN, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime beginTime;

    /**
     * The ending time of this reservation.
     */
    @Column(name = DatabaseConsts.RESERVATION_END_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private LocalDateTime endTime;

    /**
     * The list of parking events associated with this reservation.
     */
    @NotNull(message = ReservationMessages.LIST_OF_PARKING_EVENTS_NULL)
    @OneToMany(mappedBy = DatabaseConsts.RESERVATION_TABLE, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @Getter
    private final List<ParkingEvent> parkingEvents = new ArrayList<>();

    @NotNull(message = ReservationMessages.STATUS_NULL)
    @Column(name = DatabaseConsts.RESERVATION_STATUS_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private ReservationStatus status;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = ReservationMessages.CREATION_TIMESTAMP_FUTURE)
    private LocalDateTime creationTime;

    /**
     * Identity of the user creating entity object in the database.
     * Basically, this is user login taken from SecurityContext when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATED_BY, updatable = false)
    private String createdBy;

    /**
     * Time of the update of the entity object in the database.
     * Basically, this time is saved when updating object to the database.
     */
    @Column(name = DatabaseConsts.UPDATE_TIMESTAMP)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = ReservationMessages.UPDATE_TIMESTAMP_FUTURE)
    private LocalDateTime updateTime;

    /**
     * Identity of the user updating entity object in the database.
     * Basically, this is user login taken from SecurityContext when updating object in the database.
     */
    @Column(name = DatabaseConsts.UPDATED_BY)
    private String updatedBy;

    /**
     * Constructs a new reservation for a non-anonymous client.
     * @param client The Client on behalf of whom the reservation is made.
     * @param sector Sector in which the parking spot in allocated.
     * @param beginTime Start time of the reservation.
     */
    public Reservation(Client client, Sector sector, LocalDateTime beginTime) {
        this.client = client;
        this.sector = sector;
        this.beginTime = beginTime;
        this.status = ReservationStatus.AWAITING;
    }

    /**
     * Constructs a new reservation for a non-anonymous client.
     * @param sector Sector in which the parking spot in allocated.
     * @param beginTime Start time of the reservation.
     */
    public Reservation(Sector sector, LocalDateTime beginTime) {
        this(null, sector, beginTime);
        this.status = ReservationStatus.AWAITING;
    }

    /**
     * Adds new parking event to the reservation.
     *
     * @param parkingEvent Parking event to be added to the reservation.
     */
    public void addParkingEvent(ParkingEvent parkingEvent) {
        this.parkingEvents.add(parkingEvent);
        parkingEvent.setReservation(this);
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the reservation object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }

    @PrePersist
    private void beforePersistingToTheDatabase() {
        this.creationTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = authentication.getName();
        }
    }

    @PreUpdate
    private void beforeUpdatingInTheDatabase() {
        this.updateTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.updatedBy = authentication.getName();
        }
    }
}
