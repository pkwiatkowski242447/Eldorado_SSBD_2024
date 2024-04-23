import com.github.dockerjava.api.model.HostConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestcontainersConfig {

    static final String testDBName = "testDB";

    ///TODO w przypadku zmiany tworzonego usera w nominalnym dockerze tutaj tez zmienimy :D
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withCreateContainerCmdModifier(cmd -> {
                        cmd.withName(testDBName);
                        cmd.withHostName(testDBName);
                        cmd.withHostConfig(HostConfig.newHostConfig().withPortBindings(
                                new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
                        );
                    }
            )
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withCopyFileToContainer(MountableFile.forClasspathResource("sql/init_struct_test.sql"),
                    "/docker-entrypoint-initdb.d/");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        assertNotNull(connectionProvider.getConnection());
    }

    @Test
    void testPostgresContainer() throws InterruptedException {
        System.out.println(postgres.getJdbcUrl());

        Thread.sleep(100000L);
    }
}
