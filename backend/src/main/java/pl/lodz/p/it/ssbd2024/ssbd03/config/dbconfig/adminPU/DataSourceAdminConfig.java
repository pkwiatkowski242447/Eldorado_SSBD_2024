package pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.adminPU;

import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;

@Configuration
public class DataSourceAdminConfig {

    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.admin.username}")
    private String username;
    @Value("${jdbc.ssbd03.admin.password}")
    private String password;
    @Value("${jdbc.ssbd03.admin.max_pool_size}")
    private Integer maxPoolSize;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Bean(DatabaseConfigConstants.DS_ADMIN)
    public AtomikosNonXADataSourceBean dataSource() {
        AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setLocalTransactionMode(true);
        dataSource.setTestQuery("SELECT 1");
        dataSource.setUniqueResourceName("ADMIN_ATOMIKOS_CP");
        return dataSource;
    }
}
