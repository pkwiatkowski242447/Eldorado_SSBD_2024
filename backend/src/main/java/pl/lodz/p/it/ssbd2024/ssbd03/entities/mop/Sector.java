package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.SectorConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.SectorMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entity representing a Sector of a Parking.
 * @see Parking
 * @see Reservation
 */
@Entity
@Table(
        name = DatabaseConsts.SECTOR_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.SECTOR_PARKING_ID_INDEX, columnList = DatabaseConsts.SECTOR_PARKING_ID_COLUMN)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = DatabaseConsts.SECTOR_NAME_PARKING_ID_UNIQUE_KEY,
                        columnNames = {DatabaseConsts.SECTOR_NAME_COLUMN, DatabaseConsts.SECTOR_PARKING_ID_COLUMN})
        }
)
@LoggerInterceptor
@NoArgsConstructor
@Getter
@NamedQueries({
        @NamedQuery(
                name = "Sector.findAllInParking",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.parking.id = :parkingId
                            AND (:showOnlyActive != true
                            OR (s.deactivationTime IS NULL OR s.deactivationTime > :deactivationMinimum))
                        ORDER BY s.name"""
        ),
        @NamedQuery(
                name = "Sector.findWithAvailablePlaces",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.occupiedPlaces < s.maxPlaces AND s.parking.id = :parkingId AND (:showOnlyActive != true OR s.weight > 0)
                        ORDER BY s.name"""
        ),
        @NamedQuery(
                name = "Sector.findBySectorTypes",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.type IN :sectorTypes AND :parkingId = s.parking AND (:showOnlyActive != true OR s.weight > 0)
                        ORDER BY s.parking.address.city, s.parking.address.city"""
        ),
        @NamedQuery(
                name = "Sector.findByParkingIdAndName",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.parking.id = :parkingId AND s.name = :name"""
        )
})
public class Sector extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Used to describe different types of the Sector.
     * Depending on it, access to the sector is restricted to the different Clients based on their type.
     * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client
     * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client.ClientType
     */
    public enum SectorType {COVERED, UNCOVERED, UNDERGROUND}

    /**
     * The parking to which this sector belongs.
     */
    @NotNull(message = SectorMessages.PARKING_NULL)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = DatabaseConsts.SECTOR_PARKING_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.SECTOR_PARKING_ID_FK),
            nullable = false, updatable = false
    )
    private Parking parking;

    /**
     * The name of this sector.
     */
    @Pattern(regexp = SectorConsts.SECTOR_NAME_PATTERN, message = SectorMessages.SECTOR_REGEX_NOT_MET)
    @Size(min = SectorConsts.SECTOR_NAME_LENGTH, max = SectorConsts.SECTOR_NAME_LENGTH, message = SectorMessages.SECTOR_NAME_INVALID)
    @Column(name = DatabaseConsts.SECTOR_NAME_COLUMN, nullable = false, length = 5)
    @Setter
    private String name;

    /**
     * The type of this sector, which can be COVERED, UNCOVERED, or UNDERGROUND.
     */
    @NotNull(message = SectorMessages.SECTOR_TYPE_NULL)
    @Column(name =  DatabaseConsts.SECTOR_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private SectorType type;

    /**
     * The maximum number of parking spots in this sector.
     */
    @NotNull(message = SectorMessages.SECTOR_MAX_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_MAX_PLACES_NEGATIVE)
    @Max(value = SectorConsts.SECTOR_MAX_PLACES_MAX_VALUE, message = SectorMessages.SECTOR_MAX_PLACES_FULL)
    @Column(name = DatabaseConsts.SECTOR_MAX_PLACES_COLUMN, nullable = false)
    @Setter
    private Integer maxPlaces;

    /**
     * The current number of available parking spots in this sector.
     */
    @NotNull(message = SectorMessages.SECTOR_OCCUPIED_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_OCCUPIED_PLACES_NEGATIVE)
    @Column(name = DatabaseConsts.SECTOR_OCCUPIED_PLACES_COLUMN, nullable = false)
    @Setter
    private Integer occupiedPlaces = 0;

    /**
     * The weight of this sector in spot assigning algorithms.
     */
    @NotNull(message = SectorMessages.SECTOR_WEIGHT_NULL)
    @Min(value = SectorConsts.SECTOR_WEIGHT_MIN_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_SMALL)
    @Max(value = SectorConsts.SECTOR_WEIGHT_MAX_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_LARGE)
    @Column(name = DatabaseConsts.SECTOR_WEIGHT_COLUMN, nullable = false)
    @Setter
    private Integer weight;

    // @FutureOrPresent(message = SectorMessages.DEACTIVATION_TIME_PAST)
    @Column(name = DatabaseConsts.SECTOR_DEACTIVATION_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deactivationTime;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = SectorMessages.CREATION_TIMESTAMP_FUTURE)
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
    @PastOrPresent(message = SectorMessages.UPDATE_TIMESTAMP_FUTURE)
    private LocalDateTime updateTime;

    /**
     * Identity of the user updating entity object in the database.
     * Basically, this is user login taken from SecurityContext when updating object in the database.
     */
    @Column(name = DatabaseConsts.UPDATED_BY)
    private String updatedBy;

    /**
     * Constructs a new Sector.
     * @param parking Parking in which the sector is located.
     * @param name Sector's name.
     * @param type Sector's type.
     * @param maxPlaces Total number of parking spots in the sector.
     * @param weight Sector's weight in the spot assigning algorithms. If set to 0, the sector is disabled.
     */
    public Sector(Parking parking, String name, SectorType type, Integer maxPlaces, Integer weight) {
        this.parking = parking;
        this.name = name;
        this.type = type;
        this.maxPlaces = maxPlaces;
        this.weight = weight;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the sector object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }

    /**
     * Method used to determine whether this sector is active or not.
     * It used the reservation maximum time to determine that, since sector deactivation
     * must happen at least with reservationMaxLength time ahead of current date and time.
     * @param reservationMaxLength Maximum length of the reservation in hours.
     * @return Boolean flag indicating whether this sector is active or not.
     */
    public boolean getActive(int reservationMaxLength) {
        return this.deactivationTime == null ||
                (Duration.between(this.deactivationTime, LocalDateTime.now()).toHoursPart() > reservationMaxLength);
    }

    /**
     * Method used in order to reset the active
     * status of the sector, that is to activate the sector.
     */
    public void activateSector() {
        this.deactivationTime = null;
    }

    /**
     * Method used in order to deactivate this sector,
     * setting its deactivation time.
     */
    public void deactivateSector(LocalDateTime deactivationTime) {
        this.deactivationTime = deactivationTime;
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
