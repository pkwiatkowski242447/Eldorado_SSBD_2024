package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "account")
@SecondaryTable(name = "personal_data")
@ToString(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class Account extends AbstractEntity {
    @Column(name = "login", unique = true, nullable = false, updatable = false, length = 32)
    @Setter(AccessLevel.NONE)
    private String login;

    @Column(name = "password", nullable = false, length = 64)
    @ToString.Exclude
    private String password;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "name", table = "personal_data", nullable = false, length = 32)
    private String name;

    @Column(name = "lastname", table = "personal_data", nullable = false, length = 32)
    private String lastname;

    @Column(name = "email", table = "personal_data", nullable = false, length = 64)
    private String email;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    ///TODO rozwazyc zmiane na Set
    private Collection<UserLevel> userLevels = new ArrayList<>();

    @Column(name = "last_successful_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastSuccessfulLoginTime;

    @Column(name = "last_successful_login_ip", length = 17)
    private String lastSuccessfulLoginIp;

    @Column(name = "last_unsuccessful_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUnsuccessfulLoginTime;

    @Column(name = "last_unsuccessful_login_ip", length = 17)
    private String lastUnsuccessfulLoginIp;

    @Column(name = "language", length = 16)
    private String accountLanguage;

    public Account(String login, String password, String name, String lastname, String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public void addUserLevel(UserLevel userLevel) {
        userLevels.add(userLevel);
    }
}
