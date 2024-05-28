package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.integrity.AccountDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

/**
 * Controller used for manipulating parking in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController implements ParkingControllerInterface {


    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.CLIENT, Roles.STAFF})
    public ResponseEntity<?> getSectorById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed({Roles.STAFF})
    public ResponseEntity<?> removeParkingById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed({Roles.CLIENT})
    public ResponseEntity<?> enterParkingWithReservation(String reservationId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Roles.STAFF)
    public ResponseEntity<?> editParking(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                         @Valid @RequestBody ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
