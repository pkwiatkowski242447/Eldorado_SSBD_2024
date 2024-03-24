package pl.lodz.p.it.ssbd2024.ssbd03.web;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoAdm;
import pl.lodz.p.it.ssbd2024.ssbd03.repostories.HelloRepoMOK;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepoAdm repoAdm;
    private HelloRepoMOK repoMOK;

    @Autowired
    public HelloService(HelloRepoAdm repoAdm, HelloRepoMOK repoMOK) {
        this.repoAdm = repoAdm;
        this.repoMOK = repoMOK;
    }


    public String getHello() {
        return "Hello World";
    }

    public void addTestEnt() {
        HelloEntity ent = new HelloEntity(null, "Jan", 20);
        repoAdm.save(ent);
    }
}

