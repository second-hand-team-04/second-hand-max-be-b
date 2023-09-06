package com.codesquad.secondhand.auth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private String token;

	public RefreshToken(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}

	public void updateRefreshToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
