package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "sector")
@ToString(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class Sector extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static enum SectorType {COVERED, UNCOVERED, UNDERGROUND}

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Parking parking;

    @Column(name = "name", unique = true, nullable = false, length = 5)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SectorType type;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "places", nullable = false)
    private Integer places = 0;

    @Column(name = "weight", nullable = false)
    ///FIXME przemyslec zmiane typu wagi
    private Double weight;
}
