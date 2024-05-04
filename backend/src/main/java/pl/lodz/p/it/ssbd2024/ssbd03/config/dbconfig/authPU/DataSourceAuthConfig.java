package pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.authPU;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;

@Configuration
public class DataSourceAuthConfig {

    @Value("${jdbc.ssbd03.url}")
    private String url;
    @Value("${jdbc.ssbd03.auth.username}")
    private String username;
    @Value("${jdbc.ssbd03.auth.password}")
    private String password;
    @Value("${jdbc.ssbd03.auth.max_pool_size}")
    private Integer maxPoolSize;

    @Bean(DatabaseConfigConstants.DS_AUTH)
    public AtomikosDataSourceBean dataSource(){
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(url);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setXaDataSource(pgxaDataSource);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setTestQuery("SELECT 1");
        dataSource.setUniqueResourceName("AUTH_ATOMIKOS_CP");

        return dataSource;
    }
}
