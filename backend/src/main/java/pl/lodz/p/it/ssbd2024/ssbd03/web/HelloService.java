package pl.lodz.p.it.ssbd2024.ssbd03.web;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity2;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.AccountMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOK;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOP;

import java.time.LocalDateTime;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepoMOK repoMOK;
    private HelloRepoMOP repoMOP;

    @Autowired
    public HelloService(HelloRepoMOK repoMOK, HelloRepoMOP repoMOP, AccountMOKFacade accountMOKFacade, AccountMOPFacade accountMOPFacade) {
        this.repoMOK = repoMOK;
        this.repoMOP = repoMOP;
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
}

