package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.adminPU;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class DataSourceAdminConfig {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.admin.username}")
    private String username;
    @Value("${jdbc.ssbd03.admin.password}")
    private String password;
    @Value("${jdbc.ssbd03.admin.max_pool_size}")
    private Integer maxPoolSize;

    @Bean(DatabaseConfigConstants.DS_ADMIN)
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("ADMIN_HIKARI_CP");

        return new HikariDataSource(hikariConfig);
    }

}
