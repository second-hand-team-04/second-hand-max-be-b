package com.codesquad.secondhand.auth.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.AccessTokenResponse;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;
import com.codesquad.secondhand.auth.domain.RefreshToken;
import com.codesquad.secondhand.auth.infrastrucure.RefreshTokenRepository;
import com.codesquad.secondhand.common.exception.ErrorType;
import com.codesquad.secondhand.common.exception.auth.AuthUnauthorizedException;
import com.codesquad.secondhand.common.util.AuthorizationHeaderUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public SignInResponse createToken(Account account) {
		return new SignInResponse(generateAccessToken(account), generateRefreshToken(account));
	}

	private String generateRefreshToken(Account account) {
		String token = jwtTokenProvider.generateRefreshToken(Map.of("id", account.getId()));
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(account.getId())
			.orElseGet(() -> refreshTokenRepository.save(new RefreshToken(account.getId(),token)));
		refreshToken.updateRefreshToken(token);
		return refreshToken.getToken();
	}

	private String generateAccessToken(Account account) {
		return jwtTokenProvider.generateAccessToken(Map.of("id", account.getId()));
	}

	@Transactional(readOnly = true)
	public AccessTokenResponse generateAccessToken(String authorizationHeader) {
		try {
			String refreshToken = AuthorizationHeaderUtil.getRefreshToken(authorizationHeader);
			Account account = jwtTokenProvider.generateAccount(refreshToken);
			return new AccessTokenResponse(generateAccessToken(account));
		} catch (RuntimeException e) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_REFRESH_TOKEN_UNAUTHORIZED);
		}
	}

	public void deleteRefreshToken(long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void deleteAllExpiredRefreshTokens() {
		List<Long> ids = refreshTokenRepository.findAll()
			.stream()
			.filter(r -> jwtTokenProvider.isExpired(r.getToken()))
			.map(RefreshToken::getId)
			.collect(Collectors.toUnmodifiableList());
		refreshTokenRepository.deleteAllByIdInBatch(ids);
	}
}
