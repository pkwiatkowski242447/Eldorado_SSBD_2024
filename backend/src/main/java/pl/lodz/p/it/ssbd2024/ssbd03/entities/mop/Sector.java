package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "sector", uniqueConstraints = {@UniqueConstraint(columnNames = {"parking_id", "name"})})
@ToString(callSuper = true)
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Sector.findAllInParking",
                query = "SELECT s FROM Sector s WHERE s.parking.id = :parkingId"
        )
})
public class Sector extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static enum SectorType {COVERED, UNCOVERED, UNDERGROUND}

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false, updatable = false)
    @Getter
    private Parking parking;

    @Column(name = "name", nullable = false, length = 5)
    @Getter @Setter
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private SectorType type;

    @Column(name = "max_places", nullable = false)
    @Getter @Setter
    private Integer maxPlaces;

    @Column(name = "available_places", nullable = false)
    @Getter @Setter
    private Integer availablePlaces = 0;

    @Column(name = "weight", nullable = false)
    @Getter @Setter
    private Integer weight;

    public Sector(Parking parking, String name, SectorType type, Integer maxPlaces, Integer weight) {
        this.parking = parking;
        this.name = name;
        this.type = type;
        this.maxPlaces = maxPlaces;
        this.weight = weight;
    }
}
