package pl.lodz.p.it.ssbd2024.ssbd03.transactions;

import jakarta.transaction.Synchronization;

public class TransactionSynchronization implements Synchronization {

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
    private final String transactionKey;

    public TransactionSynchronization(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    @Override
    public void beforeCompletion() {}

    @Override
    public void afterCompletion(int i) {
        System.out.println("Transaction: " + transactionKey + " completed with status: " + TransactionStatus.values()[i]);
    }
}
