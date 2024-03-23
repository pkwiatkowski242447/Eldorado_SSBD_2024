package pl.lodz.p.it.ssbd2024.ssbd03;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class HelloService {

    private HelloRepo repo;

    @Autowired
    public HelloService(HelloRepo repo) {
        this.repo = repo;
    }


    public String getHello() {
        return "Hello World";
    }

    public void addTestEnt() {
        HelloEntity ent = new HelloEntity(null, "Jan", 20);
        repo.save(ent);
    }
}

