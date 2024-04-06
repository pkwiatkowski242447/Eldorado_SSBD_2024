package pl.lodz.p.it.ssbd2024.ssbd03.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@MappedSuperclass
@ToString
public class AbstractEntity {
    @Id
    @Column(name = "id", columnDefinition = "UUID", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    @EqualsAndHashCode.Exclude
    @Getter
    private Long version;
}
