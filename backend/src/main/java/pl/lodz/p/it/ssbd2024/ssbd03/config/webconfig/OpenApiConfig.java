package pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Eldorado API", version = "v1",
        description = ""),
        servers = { @Server(url = "/", description = "Default URL")})
public class OpenApiConfig {}