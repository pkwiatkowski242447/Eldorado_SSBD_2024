package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangeEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;

import java.util.UUID;

public interface AccountControllerInterface {

    ResponseEntity<?> blockAccount(String id);
    ResponseEntity<?> unblockAccount(String id);
    ResponseEntity<?> getAllUsers(int pageNumber, int pageSize);
    ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(String login, String firstName, String lastName, boolean order, int pageNumber, int pageSize);
    ResponseEntity<?> activateAccount(String token);
    ResponseEntity<?> confirmEmail(String token);
    ResponseEntity<?> getSelf();
    ResponseEntity<?> modifySelfAccount(String ifMatch, AccountModifyDTO accountModifyDTO);
    ResponseEntity<?> getUserById(String id);
    ResponseEntity<?> changeEmail(UUID id, AccountChangeEmailDTO accountChangeEmailDTO);
    ResponseEntity<?> resendEmailConfirmation();
}
