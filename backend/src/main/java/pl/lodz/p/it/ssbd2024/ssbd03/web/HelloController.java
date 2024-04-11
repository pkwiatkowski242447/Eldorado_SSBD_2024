package pl.lodz.p.it.ssbd2024.ssbd03.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @GetMapping("/test")
    public void testEP() {
        log.info("TEST ENDPOINT");
    }
}