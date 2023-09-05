package com.codesquad.secondhand.auth.domain;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.common.exception.auth.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final long accessKeyExpire;
	private final long refreshKeyExpire;
	private final String issuer;
	private final Key key;

	public JwtTokenProvider(@Value("${jwt.token.secret-key}") String secretKey, @Value("${jwt.token.access-token-expire}") long accessKeyExpire,
		@Value("${jwt.token.refresh-token-expire}") long refreshKeyExpire, @Value("${jwt.token.issuer}") String issuer) {
		this.accessKeyExpire = accessKeyExpire;
		this.refreshKeyExpire = refreshKeyExpire;
		this.issuer = issuer;
		key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String getAccessToken(Map<String, Object> claims) {
		return createToken(claims, accessKeyExpire);
	}

	public String getRefreshToken(Map<String, Object> claims) {
		return createToken(claims, refreshKeyExpire);
	}

	private String createToken(Map<String, Object> claims, Long expireDate) {
		Date now = new Date();
		Date expire = new Date(now.getTime() + expireDate);
		return Jwts.builder()
			.setClaims(claims)
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expire)
			.signWith(key)
			.compact();
	}

	public Account getAccount(String token) {
		try {
			return new Account(getClaims(token).get("id", Long.class));
		} catch (RuntimeException e) {
			throw new AuthenticationException();
		}
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public boolean isExpired(String token) {
		try {
			getClaims(token);
			return false;
		} catch (RuntimeException e) {
			return true;
		}
	}
}
