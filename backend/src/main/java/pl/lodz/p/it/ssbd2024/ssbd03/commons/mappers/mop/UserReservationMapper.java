package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.UserReservationOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

public class UserReservationMapper {
    static public UserReservationOutputDTO toDTO(Reservation r) {
        Address parkingAddress = r.getSector().getParking().getAddress();
        return new UserReservationOutputDTO(
                r.getId(),
                parkingAddress.getCity(),
                parkingAddress.getZipCode(),
                parkingAddress.getStreet(),
                r.getSector().getName(),
                r.getBeginTime(),
                r.getEndTime()
        );
    }
}
