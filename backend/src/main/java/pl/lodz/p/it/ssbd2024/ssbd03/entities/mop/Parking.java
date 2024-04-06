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

    ///FIXME kaskada REMOVE raczej nie powinna tu wystapic cnie?
    @OneToMany(mappedBy = "parking", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<Sector> sectors = new ArrayList<>();

    ///FIXME boolean czy void
    public boolean addSector(String... args) {
        ///TODO implement
        return false;
    }

    public boolean deleteSector(String sectorName) {
        ///TODO implement
        return false;
    }

    public boolean assignClient() {
        ///TODO implement
        return false;
    }

    public void changeSectorWeight(String sectorName, Double newWeight) {
        ///TODO implement
    }
}
