package pl.lodz.p.it.ssbd2024.ssbd03.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping
    public String home() {
        return "Sweet home";
    }

    @GetMapping("/test")
    public String test() throws JsonProcessingException {
        helloService.getTest();
        return "GOOD";
    }

    @GetMapping("/add")
    public void addTestEnt() {
        helloService.addTestEnt();
    }
}
