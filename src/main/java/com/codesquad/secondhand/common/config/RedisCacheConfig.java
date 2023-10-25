package com.codesquad.secondhand.common.config;

import static com.codesquad.secondhand.common.util.CacheType.CacheName.CATEGORY;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.ITEM;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.ITEM_VIEW_COUNT;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.MY_REGION;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.WISH_ITEM;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	@Autowired
	private RedisProperties redisProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
			redisProperties.getHost(), redisProperties.getPort());
		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(60))
			.disableCachingNullValues();
	}

	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder
			.withCacheConfiguration(ITEM,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(3)))
			.withCacheConfiguration(CATEGORY,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(2)))
			.withCacheConfiguration(ITEM_VIEW_COUNT,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(2)))
			.withCacheConfiguration(MY_REGION,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(2)))
			.withCacheConfiguration(WISH_ITEM,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(2)));
	}
}
