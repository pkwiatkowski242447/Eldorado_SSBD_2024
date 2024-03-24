package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.adminPU;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class DataSourceConfig {

    static final String DATA_SOURCE_NAME = "dataSourceAdmin";

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.admin.username}")
    private String username;
    @Value("${jdbc.ssbd03.admin.password}")
    private String password;

    @Bean(DATA_SOURCE_NAME)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

//    @Bean("jdbcTemplateAdmin")
//    public JdbcTemplate jdbcTemplate(@Qualifier("dataSourceAdmin") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}
