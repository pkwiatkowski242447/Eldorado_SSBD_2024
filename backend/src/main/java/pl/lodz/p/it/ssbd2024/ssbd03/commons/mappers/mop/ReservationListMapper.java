package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.ParkingEventOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.ReservationOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.ReservationParkingEventListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

import java.util.List;
import java.util.UUID;

public class ReservationListMapper {

    public static ReservationOutputListDTO toReservationListDTO(Reservation reservation) {
        Address parkingAddress = reservation.getSector().getParking().getAddress();
        UUID clientId = reservation.getClient() != null ? reservation.getClient().getId() : null;
        return new ReservationOutputListDTO(
                reservation.getId(),
                parkingAddress.getCity(),
                parkingAddress.getZipCode(),
                parkingAddress.getStreet(),
                reservation.getSector().getName(),
                reservation.getBeginTime(),
                reservation.getEndTime(),
                clientId
        );
    }

    public static ParkingEventOutputDTO toParkingEventDTO(ParkingEvent event) {
        return new ParkingEventOutputDTO(
                event.getId(),
                event.getType().name(),
                event.getDate(),
                event.getCreatedBy()
        );
    }

    public static ReservationParkingEventListDTO toReservationParkingEventListDTO(Reservation reservation, List<ParkingEvent> listOfParkingEvents) {
        ReservationOutputListDTO reservationOutputListDTO = toReservationListDTO(reservation);
        return new ReservationParkingEventListDTO(
                reservationOutputListDTO.getId(),
                reservationOutputListDTO.getCity(),
                reservationOutputListDTO.getZipCode(),
                reservationOutputListDTO.getStreet(),
                reservationOutputListDTO.getSectorName(),
                reservationOutputListDTO.getBeginTime(),
                reservationOutputListDTO.getEndingTime(),
                reservationOutputListDTO.getClientId(),
                listOfParkingEvents.stream()
                        .map(ReservationListMapper::toParkingEventDTO)
                        .toList()
        );
    }
}
