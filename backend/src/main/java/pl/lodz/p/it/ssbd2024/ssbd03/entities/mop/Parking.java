package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.ParkingConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ParkingMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector.SectorType;

/**
 * Entity representing a Parking in the system.
 *
 * @see Sector
 */
@Entity
@Table(
        name = DatabaseConsts.PARKING_TABLE,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {DatabaseConsts.PARKING_CITY_COLUMN, DatabaseConsts.PARKING_ZIP_CODE_COLUMN, DatabaseConsts.PARKING_STREET_COLUMN})
        }
)
@LoggerInterceptor
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Parking.findAll",
                query = """
                        SELECT s.parking FROM Sector s
                        WHERE (:showOnlyActive != true OR s.weight>0)
                        GROUP BY s.parking
                        ORDER BY s.parking.address.city, s.parking.address.city"""
        ),
        @NamedQuery(
                name = "Parking.findAllParking",
                query = """
                        SELECT p FROM Parking p
                        ORDER BY p.address.city"""
        ),
        @NamedQuery(
                name = "Parking.findBySectorTypes",
                query = """
                        SELECT s.parking FROM Sector s
                        WHERE s.type IN :sectorTypes AND (:showOnlyActive != true OR s.weight>0)
                        GROUP BY s.parking
                        ORDER BY s.parking.address.city, s.parking.address.city"""
        ),
        @NamedQuery(
                name = "Parking.findWithAvailablePlaces",
                query = """
                        SELECT s.parking FROM Sector s
                        WHERE s.availablePlaces != 0 AND (:showOnlyActive != true OR s.weight>0)
                        GROUP BY s.parking
                        ORDER BY s.parking.address.city, s.parking.address.city"""
        ),
        @NamedQuery(
                name = "Parking.removeParkingById",
                query = """
                        DELETE FROM Parking p
                        WHERE p.id = :parkingId"""
        )
})
@Getter
public class Parking extends AbstractEntity {

    /**
     * The address of the parking.
     */
    @NotNull(message = ParkingMessages.ADDRESS_NULL)
    @Embedded
    @Setter
    private Address address;

    /**
     * The list of sectors in the parking.
     */
    @NotNull(message = ParkingMessages.LIST_OF_SECTORS_NULL)
    @Size(min = ParkingConsts.LIST_OF_SECTORS_MIN_SIZE, message = ParkingMessages.LIST_OF_SECTORS_EMPTY)
    @Size(max = ParkingConsts.LIST_OF_SECTORS_MAX_SIZE, message = ParkingMessages.LIST_OF_SECTORS_FULL)
    @OneToMany(mappedBy = DatabaseConsts.PARKING_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Sector> sectors = new ArrayList<>();

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = ParkingMessages.CREATION_TIMESTAMP_FUTURE)
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
    @PastOrPresent(message = ParkingMessages.UPDATE_TIMESTAMP_FUTURE)
    private LocalDateTime updateTime;

    /**
     * Identity of the user updating entity object in the database.
     * Basically, this is user login taken from SecurityContext when updating object in the database.
     */
    @Column(name = DatabaseConsts.UPDATED_BY)
    private String updatedBy;

    /**
     * Constructs a new parking
     *
     * @param address Parking's address
     */
    public Parking(Address address) {
        this.address = address;
    }

    /**
     * Add a new sector to the Parking. Sector is created and managed by the Parking.
     *
     * @param name      Sector's name.
     * @param type      Sector's type.
     * @param maxPlaces Total number of parking spots in the sector.
     * @param weight    Sector's weight in the spot assigning algorithms. If set to 0, the sector is disabled.
     */
    public void addSector(String name, SectorType type, Integer maxPlaces, Integer weight, Boolean active) {
        sectors.add(new Sector(this, name, type, maxPlaces, weight, active));
    }

    /**
     * Removes the sector from the parking.
     * Note that it doesn't mean the sector is removed from the database, because of the bidirectional relationship.
     *
     * @param sectorName
     */
    public void deleteSector(String sectorName) {
        //Replace sector list with the list without the specified sector
        sectors = sectors.stream()
                .filter(sector -> !sector.getName().equals(sectorName))
                .collect(Collectors.toList());
    }

    public void assignClient() {
        ///TODO implement
    }

    public void changeSectorWeight(String sectorName, Integer newWeight) {
        ///TODO implement
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the parking object.
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
