package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Basic Scheduler exception.
 */
public class ScheduleBaseException extends ApplicationBaseException {

    public ScheduleBaseException() {
    }

    public ScheduleBaseException(String message) {
        super(message);
    }

    public ScheduleBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleBaseException(Throwable cause) {
        super(cause);
    }
}
