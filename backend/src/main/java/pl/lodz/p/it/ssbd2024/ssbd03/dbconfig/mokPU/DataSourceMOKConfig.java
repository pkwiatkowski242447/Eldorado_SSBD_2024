package pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.mokPU;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;

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
    @Value("${jdbc.ssbd03.mok.max_pool_size}")
    private Integer maxPoolSize;

    @Bean(DatabaseConfigConstants.DS_MOK)
    public AtomikosDataSourceBean dataSource(){
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(url);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setXaDataSource(pgxaDataSource);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setTestQuery("SELECT 1");
        dataSource.setUniqueResourceName("MOK_ATOMIKOS_CP");

        return dataSource;
    }
}
