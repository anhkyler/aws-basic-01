package com.example.hs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"hs.example.controllers", "hs.example.services, hs.example.entities"}) 
public class HsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsApplication.class, args);
	}

}
