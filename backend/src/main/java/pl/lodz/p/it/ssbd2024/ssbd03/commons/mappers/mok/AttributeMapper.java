package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.AttributeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeRecord;

public class AttributeMapper {

    public static AttributeDTO toAttributeDTO(AttributeRecord attribute) {
        return new AttributeDTO(
                attribute.getAttributeName().getAttributeName(),
                attribute.getAttributeValue().getAttributeValue()
        );
    }
}
