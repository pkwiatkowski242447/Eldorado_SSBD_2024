package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.util.Set;

@Entity
@Table(
        name = DatabaseConsts.ATTRIBUTE_ASSOCIATION_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.ATTRIBUTE_RECORD_ATTRIBUTE_NAME_ID_INDEX, columnList = DatabaseConsts.ATTRIBUTE_NAME_ID_COLUMN),
                @Index(name = DatabaseConsts.ATTRIBUTE_RECORD_ATTRIBUTE_VALUE_ID_INDEX, columnList = DatabaseConsts.ATTRIBUTE_VALUE_ID_COLUMN),
        }
)
@LoggerInterceptor
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AttributeRecord extends AbstractEntity {

    /**
     * Reference to the name of the attribute.
     */
    @ManyToOne(optional = false)
    @JoinColumn(
            name = DatabaseConsts.ATTRIBUTE_NAME_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.ATTRIBUTE_RECORD_ATTRIBUTE_NAME_ID_FK)
    )
    private AttributeName attributeName;

    /**
     * Reference to the value of the attribute.
     */
    @ManyToOne(optional = false)
    @JoinColumn(
            name = DatabaseConsts.ATTRIBUTE_VALUE_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.ATTRIBUTE_RECORD_ATTRIBUTE_VALUE_ID_FK)
    )
    private AttributeValue attributeValue;

    /**
     * Set of all accounts connected to this attribute record.
     */
    @ManyToMany(mappedBy = "attributeRecords")
    private Set<Account> setOfAccounts;

    /**
     * Custom toString() method implementation, implemented in order
     * not to leak any business data.
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
