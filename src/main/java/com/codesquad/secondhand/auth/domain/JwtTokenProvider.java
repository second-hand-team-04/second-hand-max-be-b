package com.codesquad.secondhand.auth.domain;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.common.exception.auth.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-token-expire}")
	private long accessKeyExpire;

	@Value("${jwt.token.refresh-token-expire}")
	private long refreshKeyExpire;

	public String accessToken(Account account) {
		return createToken(account, accessKeyExpire);
	}

	public String refreshToken(Account account) {
		return createToken(account, refreshKeyExpire);
	}

	private String createToken(Account account, Long expireDate) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireDate);

		return Jwts.builder()
			.claim("id", account.getId())
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public Account getAccount(String token) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
			return new Account(claims.get("id", Long.class));
		} catch (RuntimeException e) {
			throw new AuthenticationException();
		}
	}
}
