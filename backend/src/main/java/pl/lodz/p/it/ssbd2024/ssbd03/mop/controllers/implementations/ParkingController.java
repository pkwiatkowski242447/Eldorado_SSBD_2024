package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.ParkingMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.ParkingOutputMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.SectorMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.SectorListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.integrity.ParkingDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.integrity.SectorDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.ParkingListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.net.URI;
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

    @Value("${created.parking.resource.url}")
    private String createdParkingResourceURL;

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
    public ResponseEntity<?> createParking(@RequestBody ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException {
        Parking parking = parkingService.createParking(parkingCreateDTO.getCity(), parkingCreateDTO.getZipCode(),
                parkingCreateDTO.getStreet());
        return ResponseEntity.created(URI.create(this.createdParkingResourceURL + parking.getId())).build();
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
    public ResponseEntity<?> getAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
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
        try {
            Sector sector = parkingService.getSectorById(UUID.fromString(id));
            SectorOutputDTO sectorOutputDTO = SectorMapper.toSectorOutputDTO(sector);

            HttpHeaders headers = new HttpHeaders();
            headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(sectorOutputDTO)));

            return ResponseEntity.ok().headers(headers).body(sectorOutputDTO);
        } catch (SectorNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException iae) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    @Override
    @RolesAllowed(Authorities.GET_PARKING)
    public ResponseEntity<?> getParkingById(String id) throws ApplicationBaseException {
        try {
            Parking parking = parkingService.getParkingById(UUID.fromString(id));
            return ResponseEntity.ok(ParkingOutputMapper.toParkingOutputDTO(parking));
        } catch (ParkingNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
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
        try {
            this.parkingService.removeParkingById(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(I18n.UUID_INVALID);
        }
        return ResponseEntity.noContent().build();
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
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(parkingModifyDTO))) {
            throw new ParkingDataIntegrityCompromisedException();
        }
        ParkingOutputDTO parkingOutputDTO = ParkingMapper.toParkingOutputDto(
                parkingService.editParking(ParkingMapper.toParking(parkingModifyDTO), parkingModifyDTO.getParkingId())
        );

        return ResponseEntity.ok().body(parkingOutputDTO);
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
                parkingService.editSector(parkingService.getSectorById(sectorModifyDTO.getId()))
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
