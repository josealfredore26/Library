package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Jose Ramírez");
        myContact.setEmail("josea.ramireze@uqvirtual.edu.co");

        Info information = new Info()
                .title("Library API")
                .version("1.0")
                .description("API for managing library operations.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}