package pl.lodz.p.it.ssbd2024.ssbd03.config.security;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.lodz.p.it.ssbd2024.ssbd03.transactions.TransactionKeyTracker;

@Configuration
public class TransactionScopeConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new TransactionBeanFactoryPostProcessor();
    }

    @Bean
    @Scope(value = "transaction")
    public TransactionKeyTracker transactionKeyTracker() {
        return new TransactionKeyTracker();
    }
}
