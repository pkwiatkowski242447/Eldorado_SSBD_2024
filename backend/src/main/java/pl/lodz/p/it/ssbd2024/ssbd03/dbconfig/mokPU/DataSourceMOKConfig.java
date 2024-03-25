package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.mokPU;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class DataSourceMOKConfig {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.mok.username}")
    private String username;
    @Value("${jdbc.ssbd03.mok.password}")
    private String password;

    @Bean(DatabaseConfigConstants.DS_MOK)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
