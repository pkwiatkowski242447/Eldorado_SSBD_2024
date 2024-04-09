package pl.lodz.p.it.ssbd2024.ssbd03.web;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity2;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOK;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOP;

import java.time.LocalDateTime;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepoMOK repoMOK;
    private HelloRepoMOP repoMOP;

    private ParkingFacade parkingFacade;

    @Autowired
    public HelloService(HelloRepoMOK repoMOK, HelloRepoMOP repoMOP, ParkingFacade parkingFacade) {
        this.repoMOK = repoMOK;
        this.repoMOP = repoMOP;
        this.parkingFacade = parkingFacade;

        Parking parking = getTestParking("a");
        parkingFacade.create(parking);

        System.out.println(parkingFacade.find(parking.getId()).orElse(null).getId());
    }

    public String getHello() {
        return "Hello World";
    }

    public void addTestEnt() {
        HelloEntity ent = new HelloEntity(null, "Jan", 20);
        repoMOK.save(ent);
        HelloEntity2 ent2 = new HelloEntity2(null, "Miroslaw", 18, LocalDateTime.now());
        repoMOP.save(ent2);
    }

    public Parking getTestParking(String seed) {
        Parking parking = new Parking();
        Address address = new Address("Lodz", "12-345", "ulica" + seed);
        parking.setAddress(address);
        parking.addSector("s1", Sector.SectorType.UNCOVERED, 100, 1);
        parking.addSector("s2", Sector.SectorType.COVERED, 120, 2);
        parking.addSector("s3", Sector.SectorType.UNDERGROUND, 20, 3);

        return parking;
    }
}

