package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;


/**
 * Controller used for manipulating parking in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController implements ParkingControllerInterface {


    @Override
    @RolesAllowed(Authorities.ADD_PARKING)
    public ResponseEntity<?> createParking(ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ADD_SECTOR)
    public ResponseEntity<?> createSector(@PathVariable("id") String parkingId, @RequestBody SectorCreateDTO sectorCreateDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_PARKING)
    public ResponseEntity<?> getAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_SECTOR)
    public ResponseEntity<?> getSectorById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_PARKING)
    public ResponseEntity<?> getParkingById(@PathVariable("id") String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ACTIVATE_SECTOR)
    public ResponseEntity<?> activateSector(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.DEACTIVATE_SECTOR)
    public ResponseEntity<?> deactivateSector(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_SECTORS)
    public ResponseEntity<?> getSectorsByParkingId(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.DELETE_PARKING)
    public ResponseEntity<?> removeParkingById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITHOUT_RESERVATION)
    public ResponseEntity<?> enterParkingWithoutReservation(String parkingId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITH_RESERVATION)
    public ResponseEntity<?> enterParkingWithReservation(String reservationId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EDIT_PARKING)
    public ResponseEntity<?> editParking(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                         @Valid @RequestBody ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EDIT_SECTOR)
    public ResponseEntity<?> editSector(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                        @Valid @RequestBody SectorModifyDTO sectorModifyDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.DELETE_SECTOR)
    public ResponseEntity<?> removeSectorById(String sectorId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_AVAILABLE_PARKING)
    public ResponseEntity<?> getAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
         throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EXIT_PARKING)
    public ResponseEntity<?> exitParking(String reservationId, String exitCode) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
