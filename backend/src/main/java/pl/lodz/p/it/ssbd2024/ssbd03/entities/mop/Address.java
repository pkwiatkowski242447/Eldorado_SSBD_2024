package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "zipCode", nullable = false, length = 6)
    // XX-XXX pattern
    private String zipCode;

    @Column(name = "street", nullable = false)
    private String street;
}
