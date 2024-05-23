package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.RefreshTokenDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RefreshTokenDTOTest {

    private String refreshTokenNo1;
    private RefreshTokenDTO refreshTokenDTONo1;

    @BeforeEach
    public void init() {
        refreshTokenNo1 = "exampleRefreshTokenNo1";
        refreshTokenDTONo1 = new RefreshTokenDTO(refreshTokenNo1);
    }

    @Test
    public void refreshTokenDTONoArgsConstructorTestPositive() {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        assertNotNull(refreshTokenDTO);
    }

    @Test
    public void refreshTokenDTOAllArgsConstructorAndGettersTestPositive() {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO(refreshTokenNo1);
        assertNotNull(refreshTokenDTO);
        assertEquals(refreshTokenNo1, refreshTokenDTO.getRefreshToken());
    }

    @Test
    public void refreshTokenDTORefreshTokenSetterTestPositive() {
        String refreshTokenBefore = refreshTokenDTONo1.getRefreshToken();
        assertNotNull(refreshTokenBefore);

        String newRefreshToken = "NewRefreshTokenNo1";
        refreshTokenDTONo1.setRefreshToken(newRefreshToken);

        String refreshTokenAfter = refreshTokenDTONo1.getRefreshToken();
        assertNotNull(refreshTokenAfter);

        assertNotEquals(refreshTokenBefore, refreshTokenAfter);
        assertNotEquals(refreshTokenBefore, newRefreshToken);
        assertEquals(newRefreshToken, refreshTokenAfter);
    }
}
