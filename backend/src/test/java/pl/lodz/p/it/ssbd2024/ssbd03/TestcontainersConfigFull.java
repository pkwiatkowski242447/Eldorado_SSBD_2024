package pl.lodz.p.it.ssbd2024.ssbd03;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.core.io.support.ResourcePropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Testcontainers
public class TestcontainersConfigFull {

    static PostgreSQLContainer<?> postgres;
    static GenericContainer<?> tomcat;

    @BeforeAll
    static void beforeAll() throws IOException {
        ResourcePropertySource props = new ResourcePropertySource("classpath:testcontainers-config.properties");
        String postgresVersion  = (String) Objects.requireNonNullElse(props.getProperty("postgres.version"), "latest");
        String postgresUser     = (String) Objects.requireNonNullElse(props.getProperty("postgres.user"), "postgres");
        String postgresPass     = (String) Objects.requireNonNullElse(props.getProperty("postgres.pass"), "postgres");
        String postgresDB       = (String) Objects.requireNonNullElse(props.getProperty("postgres.db"), "postgres");
        String postgresHost     = (String) Objects.requireNonNullElse(props.getProperty("postgres.host"), "db");
        int postgresBindPort    = Integer.parseInt((String) Objects.requireNonNullElse(props.getProperty("postgres.port"), "5432"));

        String tomcatVersion    = (String) Objects.requireNonNullElse(props.getProperty("tomcat.version"), "latest");;
        String tomcatHost       = (String) Objects.requireNonNullElse(props.getProperty("tomcat.host"), "appServ");;
        int tomcatBindPort      = Integer.parseInt((String) Objects.requireNonNullElse(props.getProperty("tomcat.port"), "8080"));


        Network network = Network.SHARED;

        postgres = new PostgreSQLContainer<>("postgres:" + postgresVersion)
                .withUsername(postgresUser)
                .withPassword(postgresPass)
                .withDatabaseName(postgresDB)
                .withCopyFileToContainer(MountableFile.forClasspathResource("sql/init_struct_test.sql"),
                        "/docker-entrypoint-initdb.d/")
                .withCopyFileToContainer(MountableFile.forClasspathResource("integration_test_scripts/"),
                        "/scripts/")
                .withNetwork(network)
                .withNetworkAliases(postgresHost)
                .waitingFor(Wait.forListeningPort())
                .withCreateContainerCmdModifier(cmd -> {
                            cmd.getHostConfig().withPortBindings(
                                    new PortBinding(Ports.Binding.bindPort(postgresBindPort),
                                            new ExposedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                            );
                        }
                )
                .withReuse(true);

        tomcat = new GenericContainer<>("tomcat:" + tomcatVersion)
                .dependsOn(postgres)
                .withNetwork(network)
                .withNetworkAliases(tomcatHost)
                .withCopyFileToContainer(
                        MountableFile.forHostPath(Path.of("target/ROOT.war")),
                        "/usr/local/tomcat/webapps/ROOT.war")
                .withCreateContainerCmdModifier(cmd -> {
                            cmd.getHostConfig().withPortBindings(
                                    new PortBinding(Ports.Binding.bindPort(tomcatBindPort),
                                            new ExposedPort(8080))
                            );
                        }
                )
                .withReuse(true);


        postgres.start();
        tomcat.start();

        // Enable logging Postgres logs
//        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LoggerFactory.getLogger("TestcontainersConfig"));
//        postgres.followOutput(logConsumer);

        // Enable logging Tomcat logs
//        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LoggerFactory.getLogger("TestcontainersConfig"));
//        tomcat.followOutput(logConsumer);
    }

    @AfterAll
    static void afterAll() {
        tomcat.stop();
        postgres.stop();
    }

    protected void resetDatabase() throws IOException, InterruptedException {
        postgres.execInContainer("psql -U ssbd03admin -d ssbd03 -a -f scripts/reset_db.sql".split(" "));
        postgres.execInContainer("psql -U ssbd03admin -d ssbd03 -a -f scripts/init_data.sql".split(" "));
    }
}
