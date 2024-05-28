package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

/**
 * Enum class used to represent operation types and
 * their corresponding internationalization keys.
 */
public enum OperationType {

    REGISTRATION("operation.type.registration"),
    LOGIN("operation.type.login"),
    ACTIVATION("operation.type.activation"),
    BLOCK("operation.type.block"),
    UNBLOCK("operation.type.unblock"),
    PASSWORD_CHANGE("operation.type.password.change"),
    EMAIL_CHANGE("operation.type.email.change"),
    SUSPEND("operation.type.suspend"),
    RESTORE_ACCESS("operation.type.restore.access"),
    PERSONAL_DATA_MODIFICATION("operation.type.personal.data.modification");

    private final String operationName;

    OperationType(String operationName) {
        this.operationName = operationName;
    }

    public String toString() {
        return this.operationName;
    }
}
