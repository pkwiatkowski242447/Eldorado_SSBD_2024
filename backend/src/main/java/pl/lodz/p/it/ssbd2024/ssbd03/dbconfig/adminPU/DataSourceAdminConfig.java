package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.adminPU;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

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
    public AtomikosDataSourceBean dataSource() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(url);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setXaDataSource(pgxaDataSource);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setTestQuery("SELECT 1");
        dataSource.setUniqueResourceName("ADMIN_ATOMIKOS_CP");

        return dataSource;
    }

}
