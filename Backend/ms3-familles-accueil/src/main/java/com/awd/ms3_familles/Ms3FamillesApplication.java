package com.awd.ms3_familles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Ms3FamillesApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ms3FamillesApplication.class, args);
	}

}
