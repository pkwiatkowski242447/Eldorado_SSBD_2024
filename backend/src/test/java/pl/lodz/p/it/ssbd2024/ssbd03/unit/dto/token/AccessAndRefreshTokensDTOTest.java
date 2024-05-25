package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.AccessAndRefreshTokensDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccessAndRefreshTokensDTOTest {

    private String accessTokenNo1;
    private String refreshTokenNo1;
    private AccessAndRefreshTokensDTO accessAndRefreshTokensDTONo1;

    @BeforeEach
    public void init() {
        accessTokenNo1 = "exampleAccessTokenNo1";
        refreshTokenNo1 = "exampleRefreshTokenNo1";
        accessAndRefreshTokensDTONo1 = new AccessAndRefreshTokensDTO(accessTokenNo1, refreshTokenNo1);
    }

    @Test
    public void accessAndRefreshTokensDTONoArgsConstructorTestPositive() {
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO();
        assertNotNull(accessAndRefreshTokensDTO);
    }

    @Test
    public void accessAndRefreshTokensDTOAllArgsConstructorAndGettersTestPositive() {
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO(accessTokenNo1, refreshTokenNo1);
        assertNotNull(accessAndRefreshTokensDTO);
        assertEquals(accessTokenNo1, accessAndRefreshTokensDTO.getAccessToken());
        assertEquals(refreshTokenNo1, accessAndRefreshTokensDTO.getRefreshToken());
    }

    @Test
    public void accessAndRefreshTokensDTOAccessTokenSetterTestPositive() {
        String accessTokenBefore = accessAndRefreshTokensDTONo1.getAccessToken();
        assertNotNull(accessTokenBefore);

        String newAccessToken = "NewAccessTokenNo1";
        accessAndRefreshTokensDTONo1.setAccessToken(newAccessToken);

        String accessTokenAfter = accessAndRefreshTokensDTONo1.getAccessToken();
        assertNotNull(accessTokenAfter);

        assertNotEquals(accessTokenBefore, accessTokenAfter);
        assertNotEquals(accessTokenBefore, newAccessToken);
        assertEquals(newAccessToken, accessTokenAfter);
    }

    @Test
    public void accessAndRefreshTokensDTORefreshTokenSetterTestPositive() {
        String refreshTokenBefore = accessAndRefreshTokensDTONo1.getRefreshToken();
        assertNotNull(refreshTokenBefore);

        String newRefreshToken = "NewRefreshTokenNo1";
        accessAndRefreshTokensDTONo1.setRefreshToken(newRefreshToken);

        String refreshTokenAfter = accessAndRefreshTokensDTONo1.getRefreshToken();
        assertNotNull(refreshTokenAfter);

        assertNotEquals(refreshTokenBefore, refreshTokenAfter);
        assertNotEquals(refreshTokenBefore, newRefreshToken);
        assertEquals(newRefreshToken, refreshTokenAfter);
    }
}
