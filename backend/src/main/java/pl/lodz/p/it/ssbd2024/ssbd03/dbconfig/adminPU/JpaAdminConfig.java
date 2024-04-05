package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.adminPU;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@PropertySource(value = {"classpath:application.properties"})
@EnableTransactionManagement
@EnableJpaRepositories(
        value = DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN,
        entityManagerFactoryRef = DatabaseConfigConstants.EMF_ADMIN,
        transactionManagerRef = DatabaseConfigConstants.TXM
)
public class JpaAdminConfig {

    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;
    @Value("hibernate.transaction.jta.platform")
    private String transactionJtaPlatform;
    @Value("${hibernate.hbm2ddl.import_files}")
    private String importFiles;

    private Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", formatSql);
        properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        properties.put("hibernate.transaction.jta.platform", transactionJtaPlatform);
        properties.put("hibernate.hbm2ddl.import_files", importFiles);

        return properties;
    }

    @Bean(DatabaseConfigConstants.EMF_ADMIN)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(DatabaseConfigConstants.DS_ADMIN) DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPersistenceUnitName(DatabaseConfigConstants.ADMIN_PU);
        ///TODO atomicos vendor???
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPackagesToScan(DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN);
        entityManagerFactory.setJpaProperties(this.properties());
        return entityManagerFactory;
    }
}