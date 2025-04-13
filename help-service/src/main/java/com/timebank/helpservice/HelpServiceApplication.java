package com.timebank.helpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HelpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpServiceApplication.class, args);
	}

}
