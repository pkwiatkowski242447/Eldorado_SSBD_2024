package pl.lodz.p.it.ssbd2024.ssbd03;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class TestcontainersConfig {

    @Container
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withCopyFileToContainer(MountableFile.forClasspathResource("sql/init_struct_test.sql"),
                    "/docker-entrypoint-initdb.d/")
            .withReuse(true);


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
