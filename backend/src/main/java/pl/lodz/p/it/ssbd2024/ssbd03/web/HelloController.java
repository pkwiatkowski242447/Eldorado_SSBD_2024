package pl.lodz.p.it.ssbd2024.ssbd03.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class HelloController {

    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping
    public String getEmpty() {
        return "";
    }

    @GetMapping("/a")
    public String getHello() {
        return helloService.getHello();
    }

    @GetMapping("/b")
    public String getHello2(@RequestParam(name = "parkingId") UUID parkingId, @RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize) {
        return helloService.getHello2(parkingId,pageNumber,pageSize);
    }

    @GetMapping("/add")
    public void addTestEnt() {
        helloService.addTestEnt();
    }
}
