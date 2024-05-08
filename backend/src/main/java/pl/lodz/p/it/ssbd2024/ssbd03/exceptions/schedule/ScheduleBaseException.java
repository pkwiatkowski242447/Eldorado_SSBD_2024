package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule;

/**
 * Basic Scheduler exception.
 */
public class ScheduleBaseException extends Exception {

    public ScheduleBaseException(String message) {
        super(message);
    }

    public ScheduleBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
