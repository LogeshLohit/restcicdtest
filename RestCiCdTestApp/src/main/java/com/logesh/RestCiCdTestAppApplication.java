package com.logesh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RestCiCdTestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestCiCdTestAppApplication.class, args);
	}

}
