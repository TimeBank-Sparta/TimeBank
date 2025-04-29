package com.timebank.helpservice.helper.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisHelperRepositoryImpl implements RedisHelperRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final String helpersPrefix = "helpRequestId:";

}
