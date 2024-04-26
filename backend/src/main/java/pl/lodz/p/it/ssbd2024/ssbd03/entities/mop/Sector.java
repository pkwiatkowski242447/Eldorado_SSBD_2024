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
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.SectorConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.SectorMessages;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing a Sector of a Parking.
 * @see Parking
 * @see Reservation
 */
@Entity
@Table(name = DatabaseConsts.SECTOR_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Sector.findAllInParking",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.parking.id = :parkingId AND (:showOnlyActive != true OR s.weight>0)
                        ORDER BY s.name"""
        ),
        @NamedQuery(
                name = "Sector.findWithAvailablePlaces",
                query = """
                        SELECT s FROM Sector s
                        WHERE s.availablePlaces != 0 AND s.parking.id = :parkingId AND (:showOnlyActive != true OR s.weight>0)
                        ORDER BY s.name"""
        ),
        @NamedQuery(
                name = "Sector.findBySectorTypes",
                query = """
                        SELECT s FROM Sector s 
                        WHERE s.type IN :sectorTypes AND :parkingId = s.parking AND (:showOnlyActive != true OR s.weight>0) 
                        ORDER BY s.parking.address.city, s.parking.address.city"""
        )
})
public class Sector extends AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Used to describe different types of the Sector.
     * Depending on it, access to the sector is restricted to the different Clients based on their type.
     * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client
     * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client.ClientType
     */
    public enum SectorType {COVERED, UNCOVERED, UNDERGROUND}

    @NotNull(message = SectorMessages.PARKING_NULL)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.SECTOR_PARKING_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    private Parking parking;

    @Pattern(regexp = SectorConsts.SECTOR_NAME_PATTERN, message = SectorMessages.SECTOR_REGEX_NOT_MET)
    @Size(min = SectorConsts.SECTOR_NAME_LENGTH, max = SectorConsts.SECTOR_NAME_LENGTH, message = SectorMessages.SECTOR_NAME_INVALID)
    @Column(name = DatabaseConsts.SECTOR_NAME_COLUMN, unique = true, nullable = false, length = 5)
    @Getter @Setter
    private String name;

    @NotNull(message = SectorMessages.SECTOR_TYPE_NULL)
    @Column(name =  DatabaseConsts.SECTOR_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private SectorType type;

    @NotNull(message = SectorMessages.SECTOR_MAX_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_MAX_PLACES_NEGATIVE)
    @Max(value = SectorConsts.SECTOR_MAX_PLACES_MAX_VALUE, message = SectorMessages.SECTOR_MAX_PLACES_FULL)
    @Column(name = DatabaseConsts.SECTOR_MAX_PLACES_COLUMN, nullable = false)
    @Getter @Setter
    private Integer maxPlaces;

    @NotNull(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NEGATIVE)
    @Column(name = DatabaseConsts.SECTOR_AVAILABLE_PLACES_COLUMN, nullable = false)
    @Getter @Setter
    private Integer availablePlaces = maxPlaces;

    @NotNull(message = SectorMessages.SECTOR_WEIGHT_NULL)
    @Min(value = SectorConsts.SECTOR_WEIGHT_MIN_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_SMALL)
    @Max(value = SectorConsts.SECTOR_WEIGHT_MAX_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_LARGE)
    @Column(name = DatabaseConsts.SECTOR_WEIGHT_COLUMN, nullable = false)
    @Getter @Setter
    private Integer weight;

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
        this.availablePlaces = maxPlaces;
        this.weight = weight;
    }
}
