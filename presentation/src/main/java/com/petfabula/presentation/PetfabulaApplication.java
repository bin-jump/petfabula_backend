package com.petfabula.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableCaching
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@SpringBootApplication
@ComponentScan("com.petfabula")
@EnableJpaRepositories("com.petfabula")
@EntityScan("com.petfabula")
public class PetfabulaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetfabulaApplication.class, args);
    }

}
