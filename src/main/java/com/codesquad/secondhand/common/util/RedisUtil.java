package com.codesquad.secondhand.common.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {

	public static final String CATEGORY = "category";
	public static final String ITEM = "item";
	public static final String ITEM_VIEW_COUNT = "itemViewCount";
	public static final String MY_REGION = "myRegion";
	public static final String WISH_ITEM = "wishItem";

	private final RedisTemplate<String, String> redisValueStringTemplate;
	private final RedisTemplate<String, Object> redisValueObjectTemplate;
	private final CacheManager cacheManager;

	public Long incrementForValue(String cacheType, Long key, long incrementValue) {
		ValueOperations<String, String> operations = redisValueStringTemplate.opsForValue();
		return operations.increment(createCacheKey(cacheType, key), incrementValue);
	}

	public Long incrementForHash(String cacheType, Long key, String hashKey, long incrementValue) {
		HashOperations<String, String, Object> operations = redisValueObjectTemplate.opsForHash();
		return operations.increment(createCacheKey(cacheType, key), hashKey, incrementValue);
	}

	public void putForHash(String cacheType, Long key, String hashKey, Object value) {
		HashOperations<String, String, Object> operations = redisValueObjectTemplate.opsForHash();
		operations.put(createCacheKey(cacheType, key), hashKey, value);
	}

	public void putForString(String cacheType, Long key, String value) {
		ValueOperations<String, String> operations = redisValueStringTemplate.opsForValue();
		operations.set(createCacheKey(cacheType, key), value);
	}

	public <T> T getForHash(String cacheType, Long key, String hashKey, Class<T> returnType) {
		if (Objects.isNull(returnType)) {
			throw new IllegalArgumentException();
		}

		HashOperations<String, String, Object> hashOperations = redisValueObjectTemplate.opsForHash();
		return (T) hashOperations.get(createCacheKey(cacheType, key), hashKey);
	}

	public String getForString(String cacheType, Long key) {
		ValueOperations<String, String> operations = redisValueStringTemplate.opsForValue();
		return operations.get(createCacheKey(cacheType, key));
	}

	public void expiredForObject(String cacheType, Long key, Duration duration) {
		redisValueObjectTemplate.expire(createCacheKey(cacheType, key), duration);
	}

	public <T> Map<Long, T> getKeyValueMapForObjectAndCacheManager(String key, Class<T> valueType) {
		if (Objects.isNull(valueType)) {
			throw new IllegalArgumentException();
		}

		ScanOptions scanOptions = ScanOptions.scanOptions()
			.match(String.format("%s*", key))
			.count(100)
			.build();
		Cursor<String> cursor = redisValueObjectTemplate.scan(scanOptions);
		Cache cache = cacheManager.getCache(key);
		Map<Long, T> result = new HashMap<>();

		while (cursor.hasNext()) {
			String data = cursor.next();
			Long itemId = Long.valueOf(data.split("::")[1]);
			result.put(itemId, cache.get(itemId, valueType));
		}

		return result;
	}

	public <T> T getCacheObject(String cacheType, Long key, Class<T> valueType) {
		return cacheManager.getCache(cacheType)
			.get(key, valueType);
	}

	private String createCacheKey(String cacheType, Long key) {
		return String.format("%s::%d", cacheType, key);
	}
}

