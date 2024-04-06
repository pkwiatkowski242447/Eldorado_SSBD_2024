package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking")
@ToString(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class Parking extends AbstractEntity {

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "parking", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<Sector> sectors = new ArrayList<>();

    public void addSector(String... args) {
        ///TODO implement
    }

    public void deleteSector(String sectorName) {
        ///TODO implement
    }

    public void assignClient() {
        ///TODO implement
    }

    public void changeSectorWeight(String sectorName, Integer newWeight) {
        ///TODO implement
    }
}
