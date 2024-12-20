package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.authentication;

/**
 * Used to specify an Exception related with updating an ActivityLog.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog
 */
public class ActivityLogUpdateException extends AuthenticationBaseException {
    public ActivityLogUpdateException(String message) {
        super(message);
    }
}
