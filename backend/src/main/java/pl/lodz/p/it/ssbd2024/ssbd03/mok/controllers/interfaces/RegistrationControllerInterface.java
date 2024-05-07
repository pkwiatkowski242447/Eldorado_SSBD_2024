package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;

/**
 * Interface used for managing registration.
 */
public interface RegistrationControllerInterface {

    /**
     * Allows to create new account with client user level.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> registerClient(AccountRegisterDTO accountRegisterDTO);

    /**
     * Allows to create new account with staff user level.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> registerStaff(AccountRegisterDTO accountRegisterDTO);

    /**
     * Allows to create new account with admin user level.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> registerAdmin(AccountRegisterDTO accountRegisterDTO);
}
