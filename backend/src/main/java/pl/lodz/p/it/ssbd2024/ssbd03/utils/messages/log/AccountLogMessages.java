package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.log;

public class AccountLogMessages {
    // INFO
    public static final String ACCOUNT_BLOCKED_INFO = "Account %s has been blocked.";

    // ERROR
    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "Account with given ID could not be found.";
    public static final String ACCOUNT_ALREADY_BLOCKED_EXCEPTION = "This account is already blocked.";
    public static final String ACCOUNT_ALREADY_UNBLOCKED_EXCEPTION = "This account is already unblocked.";
    public static final String ACCOUNT_INVALID_UUID_EXCEPTION = "Invalid UUID passed.";
    public static final String ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION = "You cannot block your own account";
}
