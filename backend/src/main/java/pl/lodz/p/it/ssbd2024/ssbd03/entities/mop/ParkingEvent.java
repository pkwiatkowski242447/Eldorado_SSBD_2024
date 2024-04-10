package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ParkingEventMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = DatabaseConsts.PARKING_EVENT_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor
public class ParkingEvent extends AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static enum EventType {ENTRY, EXIT}

    @NotNull(message = ParkingEventMessages.RESERVATION_NULL)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.PARKING_EVENT_RESERVATION_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    private Reservation reservation;

    @NotNull(message = ParkingEventMessages.DATE_NULL)
    @Column(name = DatabaseConsts.PARKING_EVENT_DATE_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter
    private LocalDateTime date;

    @NotNull(message = ParkingEventMessages.EVENT_TYPE_NULL)
    @Column(name = DatabaseConsts.PARKING_EVENT_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private EventType type;
}
