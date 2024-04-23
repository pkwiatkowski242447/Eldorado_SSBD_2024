import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

public class TestcontainersConfig {

    ///TODO w przypadku zmiany tworzonego usera w nominalnym dockerze tutaj tez zmienimy :D
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
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

        System.out.println(connectionProvider.getConnection() != null);
    }

    @Test
    void testPostgresContainer() throws InterruptedException {
        System.out.println(postgres.getJdbcUrl());

        Thread.sleep(100000L);
    }
}
