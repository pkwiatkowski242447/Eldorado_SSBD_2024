package pl.lodz.p.it.ssbd2024.ssbd03.web;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity2;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoAdm;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOP;

import java.time.LocalDateTime;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepoAdm repoAdm;
    private HelloRepoMOP repoMOP;

    @Autowired
    public HelloService(HelloRepoAdm repoAdm, HelloRepoMOP repoMOP) {
        this.repoAdm = repoAdm;
        this.repoMOP = repoMOP;
    }


    public String getHello() {
        return "Hello World";
    }

    public void addTestEnt() {
        HelloEntity ent = new HelloEntity(null, "Jan", 20);
        repoAdm.save(ent);
        HelloEntity2 ent2 = new HelloEntity2(null, "Miroslaw", 18, LocalDateTime.now());
        repoMOP.save(ent2);
    }
}

