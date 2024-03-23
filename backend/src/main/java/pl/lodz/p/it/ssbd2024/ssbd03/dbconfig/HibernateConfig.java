package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    private final DatabaseProperties dbProperties;

    @Autowired
    public HibernateConfig(DatabaseProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dbProperties.getDialect());
        properties.put("hibernate.show_sql", dbProperties.getShowSql());
        properties.put("hibernate.format_sql", dbProperties.getFormatSql());
        properties.put("hibernate.hbm2ddl.auto", dbProperties.getHbm2ddlAuto());

        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(DatabaseConfigConstants.JPA_PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(properties());

        return sessionFactory;
    }
}