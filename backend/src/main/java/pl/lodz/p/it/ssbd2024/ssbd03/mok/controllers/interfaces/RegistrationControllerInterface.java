package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing registration.
 */
public interface RegistrationControllerInterface {

    /**
     * This endpoint allows both user with administrative user level and with anonymous access to create new account with client user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     * layer of facade and service components in the application.
     */
    ResponseEntity<?> registerClient(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to create new account with staff user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     * layer of facade and service components in the application.
     */
    ResponseEntity<?> registerStaff(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to create new account with admin user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     * layer of facade and service components in the application.
     */
    ResponseEntity<?> registerAdmin(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException;
}
