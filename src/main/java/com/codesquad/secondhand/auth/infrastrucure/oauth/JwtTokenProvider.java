package com.codesquad.secondhand.auth.infrastrucure.oauth;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.auth.domain.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final long accessKeyExpire;
	private final long refreshKeyExpire;
	private final String issuer;
	private final Key key;

	public JwtTokenProvider(@Value("${jwt.token.secret-key}") String secretKey,
		@Value("${jwt.token.access-token-expire}") long accessKeyExpire,
		@Value("${jwt.token.refresh-token-expire}") long refreshKeyExpire,
		@Value("${jwt.token.issuer}") String issuer) {
		this.accessKeyExpire = accessKeyExpire;
		this.refreshKeyExpire = refreshKeyExpire;
		this.issuer = issuer;
		key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateAccessToken(Map<String, Object> claims) {
		return generateToken(claims, accessKeyExpire);
	}

	public String generateRefreshToken(Map<String, Object> claims) {
		return generateToken(claims, refreshKeyExpire);
	}

	private String generateToken(Map<String, Object> claims, Long expireDate) {
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

	public Account generateAccount(String token) {
		return new Account(getClaims(token).get("id", Long.class));
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
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
