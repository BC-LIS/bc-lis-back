package com.bclis.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "BCLIS API",
                description = "Application that offers a set of services for the efficient management, creation, and organization of documents.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Laboratorio Integrado de Sistemas - UdeA",
                        url = "https://sistemas.udea.edu.co/",
                        email = "laboratoriois@udea.edu.co"
                )
        ),
        servers = {
                @Server(
                        description = "Development server",
                        url = "http://localhost:8080/api/bclis"
                ),
                @Server(
                        description = "Production server",
                        url = "http://192.168.30.69:8080/api/bclis"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access token for the API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig { }
