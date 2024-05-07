package pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging;

import jakarta.transaction.Synchronization;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TransactionSynchronization implements Synchronization {

    /**
     * Enum containing all possible values of transaction.
     * Statuses are ordered by their numerical value as a constant in javax.transaction package.
     *
     * @see https://javaee.github.io/javaee-spec/javadocs/constant-values.html#javax.transaction.Status.STATUS_ACTIVE
     */
    public enum TransactionStatus {
        STATUS_ACTIVE,
        STATUS_MARKED_ROLLBACK,
        STATUS_PREPARED,
        STATUS_COMMITTED,
        STATUS_ROLLEDBACK,
        STATUS_UNKNOWN,
        STATUS_NO_TRANSACTION,
        STATUS_PREPARING,
        STATUS_COMMITTING,
        STATUS_ROLLING_BACK
    }

    /**
     * Transaction identifier, used for logging purposes.
     */
    private final String transactionKey;

    /**
     * Constructor, used by txAspect, in order to create a Synchronization object
     * for the transaction to be executed synchronously.
     *
     * @param transactionKey Identifier of the current transaction.
     *
     * @see TxAspect
     */
    public TransactionSynchronization(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    /**
     * The beforeCompletion method is called by the transaction manager prior to the start of the two-phase
     * transaction commit process. This call is executed with the transaction context of the transaction
     * that is being committed.
     */
    @Override
    public void beforeCompletion() {}

    /**
     * This method is called by the transaction manager after the transaction is committed or rolled back.
     * Used for logging result of the transaction.
     *
     * @param i The status of the transaction completion.
     */
    @Override
    public void afterCompletion(int i) {
        log.info("Transaction: " + transactionKey + " completed with status:" + TransactionStatus.values()[i]);
        TxAspect.getTransactionIds().remove(transactionKey);
    }
}
