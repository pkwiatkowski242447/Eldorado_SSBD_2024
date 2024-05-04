package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;

public interface AuthenticationControllerInterface {

    ResponseEntity<?> login(AccountLoginDTO accountLoginDTO, HttpServletRequest request);
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);
}
