package com.micro.loans;

import com.micro.loans.audit.AuditAwareImpl;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Loans Microservice",
                description = "This microservice contains all the CRUD operations for the Loans",
                version = "1.0",
                contact = @Contact(
                        name = "Ashish Priyadarshan",
                        email = "ashish@xyz.com",
                        url = "https://github.com/Ashishpriyadarshan"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url="https://github.com/Ashishpriyadarshan"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Loans microservice REST API's Documentation",
                url = "https://github.com/Ashishpriyadarshan"
        )

)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
