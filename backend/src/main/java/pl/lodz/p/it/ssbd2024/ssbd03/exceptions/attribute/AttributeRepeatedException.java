package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.attribute;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AttributeRepeatedException extends AttributeException {

    public AttributeRepeatedException() {
        super(I18n.ATTRIBUTE_REPEATED_EXCEPTION);
    }

    public AttributeRepeatedException(String message) {
        super(message);
    }
}
