package com.timebank.notification_service;

import org.springframework.boot.SpringApplication;

import com.timebank.common.infrastructure.config.CommonApplication;

@CommonApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
