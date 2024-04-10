package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.ParkingConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ParkingMessages;

import static pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector.SectorType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = DatabaseConsts.PARKING_TABLE,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {DatabaseConsts.PARKING_CITY_COLUMN, DatabaseConsts.PARKING_ZIP_CODE_COLUMN, DatabaseConsts.PARKING_STREET_COLUMN})
        }
)
@ToString(callSuper = true)
@NoArgsConstructor
@Getter
public class Parking extends AbstractEntity {

    @NotNull(message = ParkingMessages.ADDRESS_NULL)
    @Embedded
    @Setter
    private Address address;

    @NotNull(message = ParkingMessages.LIST_OF_SECTORS_NULL)
    @Size(min = ParkingConsts.LIST_OF_SECTORS_MIN_SIZE, message = ParkingMessages.LIST_OF_SECTORS_EMPTY)
    @Size(min = ParkingConsts.LIST_OF_SECTORS_MAX_SIZE, message = ParkingMessages.LIST_OF_SECTORS_FULL)
    @OneToMany(mappedBy = DatabaseConsts.PARKING_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private List<Sector> sectors = new ArrayList<>();

    public void addSector(String name, SectorType type, Integer maxPlaces, Integer weight) {
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
