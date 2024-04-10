package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import static pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector.SectorType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "parking", uniqueConstraints = {@UniqueConstraint(columnNames = {"city", "zip_code","street"})})
@ToString(callSuper = true)
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Parking.findAll",
                query = "SELECT p FROM Parking p"
        )
})
public class Parking extends AbstractEntity {

    @Embedded
    @Getter @Setter
    private Address address;

    @OneToMany(mappedBy = "parking", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @ToString.Exclude
    @Getter
    private List<Sector> sectors = new ArrayList<>();

    public void addSector(String name, SectorType type, Integer maxPlaces, Integer weight) {
        sectors.add(new Sector(this,name,type,maxPlaces,weight));
    }

    public void deleteSector(String sectorName) {
        //Replace sector list with the list without the specified sector
        sectors = sectors.stream().filter(sector -> !sector.getName().equals(sectorName)).collect(Collectors.toList());
    }

    public void assignClient() {
        ///TODO implement
    }

    public void changeSectorWeight(String sectorName, Integer newWeight) {
        ///TODO implement
    }
}
