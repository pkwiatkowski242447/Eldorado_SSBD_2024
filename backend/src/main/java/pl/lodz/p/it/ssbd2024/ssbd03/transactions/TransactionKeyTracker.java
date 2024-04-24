package pl.lodz.p.it.ssbd2024.ssbd03.transactions;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.TransactionSynchronizationRegistry;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionKeyTracker implements Serializable {

    @Resource(mappedName = "java:TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactionRegistry;

    @Getter
    private String transactionKey;

    @PostConstruct
    private void init() {
        transactionKey = transactionRegistry.getTransactionKey().toString();
        transactionRegistry.registerInterposedSynchronization(new TransactionSynchronization(transactionKey));
        System.out.println("TX init: "+ transactionKey);
    }
}
