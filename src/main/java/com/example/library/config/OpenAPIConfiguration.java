package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI for the library API.
 */
@Configuration
public class OpenAPIConfiguration {

    /**
     * Defines the OpenAPI configuration for the library API.
     *
     * @return OpenAPI configuration for the library API.
     */
    @Bean
    public OpenAPI defineOpenApi() {
        // Server configuration
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development environment");

        // Contact information
        Contact myContact = new Contact();
        myContact.setName("Jose Ramírez");
        myContact.setEmail("josea.ramireze@uqvirtual.edu.co");

        // General API information
        Info information = new Info()
                .title("Library API")
                .version("1.0")
                .description("API for managing library operations.")
                .contact(myContact);

        // Build and return the OpenAPI configuration
        return new OpenAPI().info(information).addServersItem(server);
    }
}
