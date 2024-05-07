package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public interface RegistrationControllerInterface {

    ResponseEntity<?> registerClient(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException;
    ResponseEntity<?> registerStaff(AccountRegisterDTO accountRegisterDTO);
    ResponseEntity<?> registerAdmin(AccountRegisterDTO accountRegisterDTO);
}
