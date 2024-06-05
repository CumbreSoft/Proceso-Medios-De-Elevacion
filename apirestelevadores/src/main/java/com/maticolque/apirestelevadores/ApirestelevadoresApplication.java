package com.maticolque.apirestelevadores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.maticolque.apirestelevadores.repository"})
@Profile("default")
public class ApirestelevadoresApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApirestelevadoresApplication.class);
		app.setAdditionalProfiles("default");
		app.run(args);
	}

}
