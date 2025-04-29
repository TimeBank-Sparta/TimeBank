package com.timebank.common.infrastructure.config;

import static java.util.concurrent.TimeUnit.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Retryer;

@Configuration
public class FeignConfig {

	@Bean
	public Retryer retryer() {
		// 0.1초의 간격으로 시작해 최대 1초의 간격으로 점점 증가하며, 최대5번 재시도한다.
		return new Retryer.Default(100L, SECONDS.toMillis(1L), 5);
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
