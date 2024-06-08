package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.UserReservationOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

public class UserHistoricalReservationListMapper {
    static public UserReservationOutputListDTO toSectorListDTO(Reservation reservation) {
        Address parkingAddress = reservation.getSector().getParking().getAddress();
        return new UserReservationOutputListDTO(
                reservation.getId(),
                parkingAddress.getCity(),
                parkingAddress.getZipCode(),
                parkingAddress.getStreet(),
                reservation.getSector().getName(),
                reservation.getBeginTime(),
                reservation.getEndTime());
    }
}
