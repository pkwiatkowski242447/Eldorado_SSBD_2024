package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.attribute;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AttributeNotFoundException extends AttributeException {

    public AttributeNotFoundException() {
        super(I18n.ATTRIBUTE_NOT_FOUND_EXCEPTION);
    }

    public AttributeNotFoundException(String message) {
        super(message);
    }
}
