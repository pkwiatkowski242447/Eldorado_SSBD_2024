package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.mopPU;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@PropertySource(value = {"classpath:application.properties"})
@EnableTransactionManagement
@EnableJpaRepositories(
        value = DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN,
        entityManagerFactoryRef = DatabaseConfigConstants.EMF_MOP,
        transactionManagerRef = DatabaseConfigConstants.TXM_MOP
)
public class JpaMOPConfig {

    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;

    private Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", formatSql);

        return properties;
    }

    ///FIXME
    @Bean(DatabaseConfigConstants.SF_MOP)
    public LocalSessionFactoryBean sessionFactory(@Qualifier(DatabaseConfigConstants.DS_MOP) DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(this.properties());

        return sessionFactory;
    }

    @Bean(DatabaseConfigConstants.EMF_MOP)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(DatabaseConfigConstants.DS_MOP) DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPersistenceUnitName(DatabaseConfigConstants.MOP_PU);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPackagesToScan(DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN);
        entityManagerFactory.setJpaProperties(this.properties());
        return entityManagerFactory;
    }

    @Bean(DatabaseConfigConstants.TXM_MOP)
    public PlatformTransactionManager transactionManager(@Qualifier(DatabaseConfigConstants.EMF_MOP) LocalContainerEntityManagerFactoryBean factoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factoryBean.getObject());
        return transactionManager;
    }
}