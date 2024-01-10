package com.plac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlacApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacApplication.class, args);
	}

}
