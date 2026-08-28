package com.alvaro_chz.macrotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MacrotrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MacrotrackerApplication.class, args);
	}

}
