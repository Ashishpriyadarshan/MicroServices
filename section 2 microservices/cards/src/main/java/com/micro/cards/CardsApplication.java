package com.micro.cards;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@OpenAPIDefinition(
info = @Info(
        title = "Cards microservice REST API Documentation",
        description = "This Swagger Documentation contains details about the cards microservice and all the CRUD Operations it offers",
        version = "V1",
        contact = @Contact(
                name = "Ashish Priyadarshan",
                email = "oggyshinchan26@gmail.com",
                url = "oggyshinchan26@gmail.com"
        ),
        license = @License(
                name = "Apache 2.0",
                url="oggyshinchan26@gmail.com"
        )
),
        externalDocs = @ExternalDocumentation(
                description = "Send me a email and i will send you the documents",
                url = "oggyshinchan26@gmail.com"
)
)
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
