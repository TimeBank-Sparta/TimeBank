package com.timebank.common.infrastructure.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootApplication  // 기본 부트 애플리케이션 설정 포함
@EnableJpaAuditing      // JPA Auditing 활성화
@EnableScheduling       // 스케줄링 활성화
@EnableFeignClients     // Feign Clients 활성화
@EnableCaching          // 캐싱 활성화
@EnableKafka
public @interface CommonApplication {
}
