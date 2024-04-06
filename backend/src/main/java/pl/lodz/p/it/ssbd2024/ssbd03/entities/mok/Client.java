package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "client_data")
@DiscriminatorValue("CLIENT")
@ToString(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class Client extends UserLevel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static enum ClientType {BASIC, STANDARD, PREMIUM}

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType type = ClientType.BASIC;
}
