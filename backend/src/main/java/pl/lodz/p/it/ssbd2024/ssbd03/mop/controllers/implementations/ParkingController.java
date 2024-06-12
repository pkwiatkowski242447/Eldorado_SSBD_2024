package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.integrity.ParkingDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.integrity.SectorDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @Value("${created.sector.resource.url}")
    private String createdSectorResourceURL;

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

    // MOP.2 - Add parking

    @Override
    @RolesAllowed({Authorities.ADD_PARKING})
    public ResponseEntity<?> createParking(@Valid ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException {
        Parking parking = parkingService.createParking(parkingCreateDTO.getCity(), parkingCreateDTO.getZipCode(),
                parkingCreateDTO.getStreet(), Parking.SectorDeterminationStrategy.valueOf(parkingCreateDTO.getStrategy()));
        return ResponseEntity.created(URI.create(this.createdParkingResourceURL + parking.getId())).build();
    }

    // MOP.6 - Create sector

    @Override
    @RolesAllowed({Authorities.ADD_SECTOR})
    public ResponseEntity<?> createSector(String parkingId, @Valid SectorCreateDTO sectorCreateDTO) throws ApplicationBaseException {
        Sector sector;
        try {
            sector = parkingService.createSector(UUID.fromString(parkingId),
                    sectorCreateDTO.getName(), Sector.SectorType.valueOf(sectorCreateDTO.getType()),
                    sectorCreateDTO.getMaxPlaces(), sectorCreateDTO.getWeight());
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.created(URI.create(this.createdSectorResourceURL + sector.getId())).build();
    }

    // MOP.1 - Get all parking

    @Override
    @RolesAllowed({Authorities.GET_ALL_PARKING})
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

    // MOP.13 - Get sector

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

    // MOP.12 - Get parking

    @Override
    @RolesAllowed({Authorities.GET_PARKING, Authorities.EDIT_PARKING})
    public ResponseEntity<?> getParkingById(String id) throws ApplicationBaseException {
        try {
            Parking parking = parkingService.getParkingById(UUID.fromString(id));
            ParkingOutputDTO parkingOutputDTO = ParkingMapper.toParkingOutputDto(parking);

            HttpHeaders headers = new HttpHeaders();
            headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(parkingOutputDTO)));

            return ResponseEntity.ok().headers(headers).body(parkingOutputDTO);
        } catch (ParkingNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    @Override
    @RolesAllowed({Authorities.GET_PARKING})
    public ResponseEntity<?> getClientSectorByParkingId(String id, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            List<SectorClientListDTO> sectors = parkingService
                    .getSectorsByParkingId(UUID.fromString(id), true, pageNumber, pageSize)
                    .stream()
                    .map(SectorClientListMapper::toSectorClientListDTO)
                    .toList();
            if (sectors.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(sectors);
        } catch (ParkingNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    // MOP.9 - Activate sector

    @Override
    @RolesAllowed({Authorities.ACTIVATE_SECTOR})
    public ResponseEntity<?> activateSector(String id) throws ApplicationBaseException {
        try {
            parkingService.activateSector(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    // MOP.10 - Deactivate sector

    @Override
    @RolesAllowed({Authorities.DEACTIVATE_SECTOR})
    public ResponseEntity<?> deactivateSector(String id, SectorDeactivationTimeDTO deactivationTimeDTO) throws ApplicationBaseException {
        try {
            parkingService.deactivateSector(UUID.fromString(id), deactivationTimeDTO.getDeactivationTime());
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    // MOP.5 - Get all sectors

    @Override
    @RolesAllowed({Authorities.GET_ALL_SECTORS})
    public ResponseEntity<?> getSectorsByParkingId(String id, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            List<SectorListDTO> sectorList = parkingService.getSectorsByParkingId(UUID.fromString(id), false, pageNumber, pageSize)
                    .stream()
                    .map(SectorListMapper::toSectorListDTO)
                    .toList();
            if (sectorList.isEmpty()) return ResponseEntity.noContent().build();
            else return ResponseEntity.ok(sectorList);
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    // MOP.3 - Remove parking

    @Override
    @RolesAllowed({Authorities.DELETE_PARKING})
    public ResponseEntity<?> removeParkingById(String id) throws ApplicationBaseException {
        try {
            this.parkingService.removeParkingById(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITHOUT_RESERVATION)
    public ResponseEntity<?> enterParkingWithoutReservation(String parkingId) throws ApplicationBaseException {
        boolean isAnonymous = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().isEmpty();

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Reservation reservation = parkingService.enterParkingWithoutReservation(UUID.fromString(parkingId), login, isAnonymous);
        return ResponseEntity.ok(UserReservationMapper.toDTO(reservation));
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITH_RESERVATION)
    public ResponseEntity<?> enterParkingWithReservation(String reservationId) throws ApplicationBaseException {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            this.parkingService.enterParkingWithReservation(UUID.fromString(reservationId), userName);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    // MOP.4 - Edit parking

    @Override
    @RolesAllowed({Authorities.EDIT_PARKING})
    public ResponseEntity<?> editParking(String ifMatch, @Valid ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(parkingModifyDTO))) {
            throw new ParkingDataIntegrityCompromisedException();
        }

        ParkingOutputDTO parkingOutputDTO = ParkingMapper.toParkingOutputDto(
                parkingService.editParking(ParkingMapper.toParking(parkingModifyDTO), parkingModifyDTO.getParkingId())
        );

        return ResponseEntity.ok(parkingOutputDTO);
    }

    // MOP.8 - Edit sector

    @Override
    @RolesAllowed({Authorities.EDIT_SECTOR})
    public ResponseEntity<?> editSector(String ifMatch, @Valid SectorModifyDTO sectorModifyDTO) throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(sectorModifyDTO))) {
            throw new SectorDataIntegrityCompromisedException();
        }

        Parking parking = parkingService.getParkingById(sectorModifyDTO.getParkingId());

        SectorOutputDTO sectorOutputDTO = SectorMapper.toSectorOutputDTO(
                parkingService.editSector(sectorModifyDTO.getId(), sectorModifyDTO.getVersion(), SectorMapper.toSector(sectorModifyDTO, parking))
        );

        return ResponseEntity.ok(sectorOutputDTO);
    }

    // MOP.7 - Remove sector

    @Override
    @RolesAllowed({Authorities.DELETE_SECTOR})
    public ResponseEntity<?> removeSectorById(String id) throws ApplicationBaseException {
        try {
            this.parkingService.removeSectorById(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    // MOP.11 - Get all available parking

    @Override
    @RolesAllowed({Authorities.GET_ALL_AVAILABLE_PARKING})
    public ResponseEntity<?> getAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        List<ParkingOutputListDTO> parkingList = parkingService.getAvailableParkingWithPagination(pageNumber, pageSize)
                .stream()
                .map(ParkingListMapper::toParkingListDTO)
                .toList();

        if (parkingList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(parkingList);
    }


    @Override
    @RolesAllowed(Authorities.EXIT_PARKING)
    public ResponseEntity<?> exitParking(String reservationId, boolean endReservation) throws ApplicationBaseException {
        try {
            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
            this.parkingService.exitParking(UUID.fromString(reservationId), userLogin, endReservation);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }
}
