package com.codesquad.secondhand.auth.infrastrucure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

	private static final Map<Long, String> STORE = new ConcurrentHashMap<>();

	public void save(long userId, String refreshToken) {
		STORE.put(userId, refreshToken);
	}

	public String get(long userId) {
		return STORE.get(userId);
	}

	public void remove(long userId) {
		STORE.remove(userId);
	}

	public boolean existsByUserId(Long userId) {
		return STORE.containsKey(userId);
	}
}
