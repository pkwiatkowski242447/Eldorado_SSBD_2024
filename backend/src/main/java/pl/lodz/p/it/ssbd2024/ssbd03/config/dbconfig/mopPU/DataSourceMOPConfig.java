package pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.mopPU;

import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;

@Configuration
public class DataSourceMOPConfig {

    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.mop.username}")
    private String username;
    @Value("${jdbc.ssbd03.mop.password}")
    private String password;
    @Value("${jdbc.ssbd03.mop.max_pool_size}")
    private Integer maxPoolSize;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Bean(DatabaseConfigConstants.DS_MOP)
    public AtomikosNonXADataSourceBean dataSource() {
        AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setLocalTransactionMode(true);
        dataSource.setTestQuery("SELECT 1");
        dataSource.setUniqueResourceName("MOP_ATOMIKOS_CP");
        return dataSource;
    }
}
