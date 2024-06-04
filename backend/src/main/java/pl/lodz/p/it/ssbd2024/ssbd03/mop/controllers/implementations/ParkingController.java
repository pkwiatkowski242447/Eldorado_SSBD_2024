package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.SectorMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.SectorListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.integrity.SectorDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.ParkingListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * Controller used for manipulating parking in the system.
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping("/api/v1/parking")
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
public class ParkingController implements ParkingControllerInterface {

    private final ParkingServiceInterface parkingService;

    /**
     * JWTProvider used for operations on JWT TOKEN.
     */
    private final JWTProvider jwtProvider;

    @Autowired
    public ParkingController(ParkingServiceInterface parkingService, JWTProvider jwtProvider) {
        this.parkingService = parkingService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @RolesAllowed(Authorities.ADD_PARKING)
    public ResponseEntity<?> createParking(ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ADD_SECTOR)
    public ResponseEntity<?> createSector(String parkingId, SectorCreateDTO sectorCreateDTO) throws ApplicationBaseException {
        parkingService.createSector(UUID.fromString(parkingId),
            sectorCreateDTO.getName(), sectorCreateDTO.getType(),
            sectorCreateDTO.getMaxPlaces(), sectorCreateDTO.getWeight(),
            sectorCreateDTO.getActive());

        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_PARKING)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAllParkingsWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        List<ParkingOutputListDTO> parkingList = parkingService.getAllParkingWithPagination(pageNumber, pageSize)
                .stream()
                .map(ParkingListMapper::toParkingListDTO)
                .toList();
        if (parkingList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(parkingList);
    }

    @Override
    @RolesAllowed(Authorities.GET_SECTOR)
    public ResponseEntity<?> getSectorById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_PARKING)
    public ResponseEntity<?> getParkingById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ACTIVATE_SECTOR)
    public ResponseEntity<?> activateSector(String id) throws ApplicationBaseException {
        parkingService.activateSector(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.DEACTIVATE_SECTOR)
    public ResponseEntity<?> deactivateSector(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_SECTORS)
    public ResponseEntity<?> getSectorsByParkingId(String id) throws ApplicationBaseException {
        List<SectorListDTO> sectorList = parkingService.getSectorsByParkingId(UUID.fromString(id))
            .stream()
            .map(SectorListMapper::toSectorListDTO)
            .toList();
        if (sectorList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(sectorList);
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
    public ResponseEntity<?> editParking(String ifMatch, ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EDIT_SECTOR)
    public ResponseEntity<?> editSector(String ifMatch, SectorModifyDTO sectorModifyDTO) throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(sectorModifyDTO))) {
            throw new SectorDataIntegrityCompromisedException();
        }

        SectorOutputDTO sectorOutputDTO = SectorMapper.toSectorOutputDTO(
                parkingService.editSector(SectorMapper.toSector(sectorModifyDTO), sectorModifyDTO.getParkingId(), sectorModifyDTO.getName())
        );
        return ResponseEntity.ok().body(sectorOutputDTO);
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
