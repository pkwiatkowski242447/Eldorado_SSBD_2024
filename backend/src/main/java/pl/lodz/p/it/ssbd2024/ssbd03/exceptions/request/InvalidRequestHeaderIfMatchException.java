package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with handling request that is missing an IF MATCH header.
 */
public class InvalidRequestHeaderIfMatchException extends InvalidRequestHeaderException {
    public InvalidRequestHeaderIfMatchException() {
        super(I18n.MISSING_HEADER_IF_MATCH);
    }
}
