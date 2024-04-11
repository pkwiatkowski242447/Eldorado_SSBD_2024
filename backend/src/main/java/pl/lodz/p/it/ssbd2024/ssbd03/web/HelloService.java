package pl.lodz.p.it.ssbd2024.ssbd03.web;

import jakarta.transaction.Transactional;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepoMOK repoMOK;
    private HelloRepoMOP repoMOP;


    @Autowired
    public HelloService(HelloRepoMOK repoMOK, HelloRepoMOP repoMOP, ParkingFacade parkingFacade) {
        this.repoMOK = repoMOK;
        this.repoMOP = repoMOP;
    }

    @Transactional
    public String getHello() {
        return "Hello World";
    }

    public void addTestEnt() {
        HelloEntity ent = new HelloEntity(null, "Jan", 20);
        repoMOK.save(ent);
        HelloEntity2 ent2 = new HelloEntity2(null, "Miroslaw", 18, LocalDateTime.now());
        repoMOP.save(ent2);
    }
}

