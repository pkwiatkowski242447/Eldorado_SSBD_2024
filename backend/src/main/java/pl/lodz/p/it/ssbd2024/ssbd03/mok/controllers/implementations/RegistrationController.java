package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.EnhancedLink;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.RegistrationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to create new Accounts in the system.
 *
 * @see Account
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping(value = "/api/v1/register")
@Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
        retryFor = {ApplicationDatabaseException.class, RollbackException.class})
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
public class RegistrationController implements RegistrationControllerInterface {

    @Value("${created.account.resource.url}")
    private String createdAccountResourceURL;

    /**
     * AccountServiceInterface used for operation on accounts.
     */
    private final AccountServiceInterface accountService;

    /**
     * Autowired constructor for the controller.
     *
     * @param accountService Service containing method for account manipulation.
     */
    @Autowired
    public RegistrationController(AccountServiceInterface accountService) {
        this.accountService = accountService;
    }

    // Register methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER})
    public ResponseEntity<?> registerClient(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account clientAccount = this.accountService.registerClient(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());

        // Created link
        Link createdLink = linkTo(methodOn(AccountController.class).getUserById(clientAccount.getId().toString())).withSelfRel();
        EnhancedLink enhancedCreatedLink = new EnhancedLink(createdLink.getHref(), createdLink.getRel(), "MOK.18", "Get user by id", "GET");
        // Login link
        Link loginLink = linkTo(AuthenticationController.class).slash("/login-using-credentials").withRel("authenticate");
        EnhancedLink enhancedLoginLink = new EnhancedLink(loginLink.getHref(), loginLink.getRel(), "MOK.2", "Authenticate", "POST");
        // List of users link
        Link listOfUsersLink = linkTo(methodOn(AccountController.class).getAllUsers(0, 5)).withRel("getAllUsers");
        EnhancedLink enhancedListOfUsersLink = new EnhancedLink(listOfUsersLink.getHref(), listOfUsersLink.getRel(), "MOK.17", "Get all users", "GET");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaTypes.HAL_JSON)
                .body(new RepresentationModel<>()
                        .add(enhancedCreatedLink, enhancedLoginLink, enhancedListOfUsersLink));
    }

    @Override
    @RolesAllowed({Authorities.REGISTER_USER})
    public ResponseEntity<?> registerStaff(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account staffAccount = this.accountService.registerStaff(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());

        // Created link
        Link createdLink = linkTo(methodOn(AccountController.class).getUserById(staffAccount.getId().toString())).withSelfRel();
        EnhancedLink enhancedCreatedLink = new EnhancedLink(createdLink.getHref(), createdLink.getRel(), "MOK.18", "Get user by id", "GET");
        // Login link
        Link loginLink = linkTo(AuthenticationController.class).slash("/login-using-credentials").withRel("authenticate");
        EnhancedLink enhancedLoginLink = new EnhancedLink(loginLink.getHref(), loginLink.getRel(), "MOK.2", "Authenticate", "POST");
        // List of users link
        Link listOfUsersLink = linkTo(methodOn(AccountController.class).getAllUsers(0, 5)).withRel("getAllUsers");
        EnhancedLink enhancedListOfUsersLink = new EnhancedLink(listOfUsersLink.getHref(), listOfUsersLink.getRel(), "MOK.17", "Get all users", "GET");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaTypes.HAL_JSON)
                .body(new RepresentationModel<>()
                        .add(enhancedCreatedLink, enhancedLoginLink, enhancedListOfUsersLink));
    }

    @Override
    @RolesAllowed({Authorities.REGISTER_USER})
    public ResponseEntity<?> registerAdmin(AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account adminAccount = this.accountService.registerAdmin(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());

        // Created link
        Link createdLink = linkTo(methodOn(AccountController.class).getUserById(adminAccount.getId().toString())).withSelfRel();
        EnhancedLink enhancedCreatedLink = new EnhancedLink(createdLink.getHref(), createdLink.getRel(), "MOK.18", "Get user by id", "GET");
        // Login link
        Link loginLink = linkTo(AuthenticationController.class).slash("/login-using-credentials").withRel("authenticate");
        EnhancedLink enhancedLoginLink = new EnhancedLink(loginLink.getHref(), loginLink.getRel(), "MOK.2", "Authenticate", "POST");
        // List of users link
        Link listOfUsersLink = linkTo(methodOn(AccountController.class).getAllUsers(0, 5)).withRel("getAllUsers");
        EnhancedLink enhancedListOfUsersLink = new EnhancedLink(listOfUsersLink.getHref(), listOfUsersLink.getRel(), "MOK.17", "Get all users", "GET");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaTypes.HAL_JSON)
                .body(new RepresentationModel<>()
                        .add(enhancedCreatedLink, enhancedLoginLink, enhancedListOfUsersLink));
    }
}
