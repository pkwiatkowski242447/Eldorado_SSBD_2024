package pl.lodz.p.it.ssbd2024.ssbd03.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/load-generation")
public class MockEndpoint {

    public record ValueDto(
            Object value
    ) {}

    @GetMapping(path = "/cpu/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ValueDto> generateCpuLoad(@PathVariable("number") int number) {
        return ResponseEntity.ok(new ValueDto(fibonacciValue(number)));
    }

    @GetMapping(path = "/memory/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ValueDto> generateMemoryLoad(@PathVariable("number") int number) {
        int[][] array = new int[number][number];
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                array[i][j] = i;
            }
        }

        return ResponseEntity.noContent().build();
    }

    private long fibonacciValue(int number) {
        if (number <= 1) return number;
        else return fibonacciValue(number - 1) + fibonacciValue(number - 2);
    }
}
