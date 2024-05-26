package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadPropertiesException;

/**
 * Interface used for managing execution of scheduled tasks.
 */
public interface ScheduleServiceInterface {

    /**
     * Removes Accounts which have not finished registration.
     * Time for the Account verification is set by <code>scheduler.not_verified_account_delete_time</code> property.
     */
    void deleteNotActivatedAccounts();

    /**
     * This method will be invoked every hour in order to check if half the time to active registered account has passed.
     * If so then new registration token will be generated, and new message for activating user account will be sent to specified e-mail address.
     */
    void resendConfirmationEmail();

    /**
     * Unblock accounts which have been blocked by login incorrectly certain amount of time.
     * Time for the Account blockade is set by <code>scheduler.blocked_account_unblock_time</code> property.
     */
    void unblockAccount();

    /**
     * Block Accounts without authentication for the last N days, where N is specified in the file consts.properties.
     *
     * @throws ScheduleBadPropertiesException Threw when problem with properties occurs.
     */
    void suspendAccountWithoutAuthenticationForSpecifiedTime() throws ScheduleBadPropertiesException;
}
