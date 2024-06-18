package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TokenUnitTest {

    private Token token;

    @BeforeEach
    public void setUp() {
        token = new Token();

    }

    @Test
    public void testToString() {
        String result = token.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
    }
}
