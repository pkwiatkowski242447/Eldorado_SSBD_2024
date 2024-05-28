package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.TokenMessages;

import java.time.LocalDateTime;

/**
 * Entity representing Token used in keeping track of various Account related actions.
 * @see Account
 */
@Entity
@Table(name = DatabaseConsts.TOKEN_TABLE)
@NoArgsConstructor
@Getter
@NamedQueries({
        @NamedQuery(
                name = "Token.findByTypeAndAccount",
                query = """
                        SELECT t FROM Token t
                        WHERE t.type = :tokenType AND t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.removeByAccount",
                query = """
                        DELETE FROM Token t
                        WHERE t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.removeByTypeAndAccount",
                query = """
                        DELETE FROM Token t
                        WHERE t.type = :tokenType AND t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.findByTokenValue",
                query = """
                        SELECT t FROM Token t
                        WHERE t.tokenValue = :tokenValue"""


        ),
        @NamedQuery(
                name = "Token.findByTokenType",
                query = """
                        SELECT t FROM Token t
                        WHERE t.type = :tokenType"""
        )
})
public class Token extends AbstractEntity {
    /**
     * Used to specify the type of the Account action related to the token.
     */
    public enum TokenType {
        REFRESH_TOKEN,
        MULTI_FACTOR_AUTHENTICATION_CODE,
        REGISTER,
        RESET_PASSWORD,
        CONFIRM_EMAIL,
        CHANGE_OVERWRITTEN_PASSWORD,
        RESTORE_ACCESS_TOKEN
    }

    /**
     * The value of the token.
     */
    @Column(name = DatabaseConsts.TOKEN_TOKEN_VALUE_COLUMN, unique = true, nullable = false, length = 512)
    @Getter @Setter
    private String tokenValue;

    /**
     * The account associated with this token.
     */
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Account account;

    /**
     * The type of the token, specifying the type of account action related to the token.
     */
    @Column(name = DatabaseConsts.TOKEN_TOKEN_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.RESET_PASSWORD;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = TokenMessages.CREATION_TIMESTAMP_FUTURE)
    private LocalDateTime creationTime;

    /**
     * Identity of the user creating entity object in the database.
     * Basically, this is user login taken from SecurityContext when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATED_BY, updatable = false)
    private String createdBy;

    /**
     * Constructs new token object using only essential data.
     *
     * @param tokenValue Value of the token to be stored in the database
     * @param account Account, which the token belongs to / is associated with.
     * @param type Type of the token.
     */
    public Token(String tokenValue, Account account, TokenType type) {
        this.tokenValue = tokenValue;
        this.account = account;
        this.type = type;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the Token object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }

    @PrePersist
    private void beforePersistingToTheDatabase() {
        this.creationTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = authentication.getName();
        }
    }
}
