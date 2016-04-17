package com.my.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * The Spring Initializer class that create a very basic Spring Boot application
 * class. Technically, this is a Spring Configuration class. The
 * annotation @SpringBootApplication enables the Spring Context and all the
 * startup magic of Spring Boot.
 * 
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
