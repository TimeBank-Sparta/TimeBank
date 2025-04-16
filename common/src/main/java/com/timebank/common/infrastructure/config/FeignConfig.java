package com.timebank.common.infrastructure.config;

import static java.util.concurrent.TimeUnit.*;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Retryer;

@Configuration
@EnableFeignClients(basePackages = "com.timebank.common.infrastructure.external")
public class FeignConfig {

	@Bean
	public Retryer retryer() {
		return new Retryer.Default(100L, SECONDS.toMillis(1L), 5); // 실패 시 최대 5번까지 재시도
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
