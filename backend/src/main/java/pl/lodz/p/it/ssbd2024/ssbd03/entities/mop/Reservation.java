package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
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
@Getter @Setter
public class Reservation extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    ///FIXME do analizy, indeks z null??
    ///oraz czy moze zostac tu kaskada PERSIST?
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @Setter(AccessLevel.NONE)
    private Client client;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sector_id", referencedColumnName = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Sector sector;

    @Column(name = "begin_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<ParkingEvent> parkingEvents = new ArrayList<>();
}
