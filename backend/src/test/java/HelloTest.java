import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.web.HelloService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTest {

    @Test
    void testHello() {
        HelloService helloService = new HelloService();
        assertEquals("Hello World", helloService.getHello());
    }
}