import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class TestcontainersConfig {

    static final String testDBName = "testDB";

    ///FIXME w przypadku zmiany tworzonego usera w nominalnym dockerze tutaj tez zmienimy :D
    @Container
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
//            .withReuse(true); //hmm?

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
