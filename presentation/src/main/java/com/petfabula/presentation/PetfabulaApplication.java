package com.petfabula.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.petfabula")
@EntityScan("com.petfabula")
public class PetfabulaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetfabulaApplication.class, args);
    }

}
