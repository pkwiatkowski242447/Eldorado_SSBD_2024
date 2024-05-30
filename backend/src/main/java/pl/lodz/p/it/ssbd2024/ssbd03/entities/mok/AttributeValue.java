package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AttributeMessages;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(
        name = DatabaseConsts.ATTRIBUTE_VALUE_TABLE,
        uniqueConstraints = @UniqueConstraint(columnNames = {DatabaseConsts.ATTRIBUTE_VALUE_COLUMN, DatabaseConsts.ATTRIBUTE_NAME_ID_COLUMN})
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Value of the attribute.
     */
    @NotBlank(message = AttributeMessages.ATTRIBUTE_VALUE_BLANK)
    @Column(name = DatabaseConsts.ATTRIBUTE_VALUE_COLUMN)
    private String attributeValue;

    /**
     * Reference to the attribute name.
     */
    @NotNull(message = AttributeMessages.ATTRIBUTE_NAME_REFERENCE_NULL)
    @ManyToOne(optional = false)
    @JoinColumn(name = DatabaseConsts.ATTRIBUTE_NAME_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    private AttributeName attributeNameId;

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
