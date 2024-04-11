package pl.lodz.p.it.ssbd2024.ssbd03.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping()
    public String getHello() {
        return helloService.getHello();
    }

    @GetMapping("/add")
    public void addTestEnt() {
        helloService.addTestEnt();
    }
}