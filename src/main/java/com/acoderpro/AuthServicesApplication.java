package com.acoderpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.acoderpro")
public class AuthServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServicesApplication.class, args);
		
		System.out.println("hello acoder.. how you doing");
	}

}
