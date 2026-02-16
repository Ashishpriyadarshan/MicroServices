package com.micro.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//incase your packages are outside or the com.micro.accounts then in that case:
//explicity mention their location here:
/*
@ComponentScans({@ComponentScan("com.micro.accounts.controller")})
@EnableJpaRepositories("com.miro.accounts.repository")
@EntityScan("com.micro.accounts.model/entity)"
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition
        (
                info = @Info(
                        title = "Accounts Microservice RESTApi Documentation",
                        description = "This Microservice contains api's for creation/fetching/updation/deletion of Customer & Account",
                        version = "V1.0",
                        contact = @Contact
                                (
                                        name = "Ashish Priyadarshan",
                                        email = "Not provided",
                                        url = "https://github.com/Ashishpriyadarshan/MicroServices/tree/main"
                                ),
                        license = @License
                                (
                                        name = "Apache 2.0",
                                        url = "https://github.com/Ashishpriyadarshan/MicroServices/tree/main"
                                )
                ),
                externalDocs = @ExternalDocumentation
                        (
                                description = "Documentation for Accounts Microservice",
                                url = "https://github.com/Ashishpriyadarshan/MicroServices/tree/main"
                        )
        )
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
