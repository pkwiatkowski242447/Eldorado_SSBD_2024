package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "account")
@SecondaryTable(name = "personal_data")
@ToString(callSuper = true)
@NoArgsConstructor
public class Account extends AbstractEntity {
    @Column(name = "login", unique = true, nullable = false, updatable = false, length = 32)
    @Getter
    private String login;

    @Column(name = "password", nullable = false, length = 60)
    @ToString.Exclude
    @Getter @Setter
    private String password;

    @Column(name = "verified", nullable = false)
    @Getter @Setter
    private Boolean verified = false;

    @Column(name = "active", nullable = false)
    @Getter @Setter
    private Boolean active = true;

    @Column(name = "name", table = "personal_data", nullable = false, length = 32)
    @Getter @Setter
    private String name;

    @Column(name = "lastname", table = "personal_data", nullable = false, length = 32)
    @Getter @Setter
    private String lastname;

    @Column(name = "email", table = "personal_data", unique = true, nullable = false, length = 64)
    @Getter @Setter
    private String email;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @Getter
    ///TODO rozwazyc zmiane na Set<>
    private Collection<UserLevel> userLevels = new ArrayList<>();

    @Embedded
    @Getter @Setter
    private ActivityLog activityLog;

    @Column(name = "language", nullable = false, length = 16)
    @Getter @Setter
    ///TODO rozwazyc tabele slownikowa
    private String accountLanguage;

    @Column(name = "phone_number", nullable = false, length = 16)
    @Getter @Setter
    private String phoneNumber;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime creationDate;

    public Account(String login, String password, String name, String lastname, String email, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addUserLevel(UserLevel userLevel) {
        userLevels.add(userLevel);
    }

    public void removeUserLevel(UserLevel userLevel) {
        userLevels.remove(userLevel);
    }
}
