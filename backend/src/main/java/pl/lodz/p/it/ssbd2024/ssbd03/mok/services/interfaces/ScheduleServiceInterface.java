package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadProperties;

/**
 * Interface used for managing execution of scheduled tasks.
 */
public interface ScheduleServiceInterface {

    /**
     * Removes Accounts which have not finished registration.
     *
     * @throws ScheduleBadProperties Threw when problem with properties occurs.
     */
    void deleteNotVerifiedAccount() throws ScheduleBadProperties;

    /**
     * This method will be invoked every n hours in order to check if half the time to active registered account has passed.
     */
    void resendConfirmationEmail();

    /**
     * Unblock Accounts which have been blocked by login incorrectly certain amount of time.
     *
     * @throws ScheduleBadProperties Threw when problem with properties occurs.
     */
    void unblockAccount() throws ScheduleBadProperties;
}
