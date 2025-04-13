package com.timebank.userservice;

import org.springframework.boot.SpringApplication;

import com.timebank.common.infrastructure.config.CommonApplication;

@CommonApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
