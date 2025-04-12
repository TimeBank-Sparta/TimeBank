package com.timebank.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import com.timebank.common.infrastructure.config.CommonApplication;

@CommonApplication
@ComponentScan(basePackages = {"com.timebank.common", "com.timebank"})
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
