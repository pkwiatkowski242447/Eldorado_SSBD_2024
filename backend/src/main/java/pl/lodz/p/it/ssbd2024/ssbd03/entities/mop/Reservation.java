package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
@ToString(callSuper = true)
@NoArgsConstructor

@NamedQueries({
        @NamedQuery(
                name = "Reservation.findActiveReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.endTime IS NULL OR CURRENT_DATE < r.endTime
                        ORDER BY r.beginTime"""
        ),
        @NamedQuery(
                name = "Reservation.findHistoricalReservations",
                query = """
                        SELECT r FROM Reservation r
                        WHERE r.endTime IS NOT NULL OR CURRENT_DATE >= r.endTime
                        ORDER BY r.beginTime"""
        )
}
)

public class Reservation extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @Getter
    private Client client;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sector_id", referencedColumnName = "id", nullable = false, updatable = false)
    @Getter
    private Sector sector;

    @Column(name = "begin_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    ///FIXME setter? chyba lepiej dac do konstruktora
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    ///FIXME ten setter imo konieczny dla wjazdu bez uprzedniej rezerwacji
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @Getter
    private List<ParkingEvent> parkingEvents = new ArrayList<>();

    public Reservation(Client client, Sector sector) {
        this.client = client;
        this.sector = sector;
    }

    public Reservation(Sector sector) {
        this(null, sector);
    }
}
