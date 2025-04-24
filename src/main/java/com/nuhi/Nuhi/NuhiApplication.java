package com.nuhi.Nuhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EnableJpaRepositories(basePackages = "com.nuhi.Nuhi.repository")
@EntityScan(basePackages = "com.nuhi.Nuhi.model")
public class NuhiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NuhiApplication.class, args);
	}

}
