package com.codesquad.secondhand.auth.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.AccessTokenResponse;
import com.codesquad.secondhand.auth.application.dto.RefreshTokenRequest;
import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.domain.RefreshToken;
import com.codesquad.secondhand.auth.infrastrucure.AuthRepository;
import com.codesquad.secondhand.auth.infrastrucure.RefreshTokenRepository;
import com.codesquad.secondhand.common.exception.auth.AuthenticationException;
import com.codesquad.secondhand.common.exception.user.UserLoginInfoDifferentException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public SignInResponse signIn(SignInRequest signInRequest) {
		Account account = authRepository.findByEmailAndPasswordAndProviderId(signInRequest.getEmail(),
				signInRequest.getPassword(), ProviderType.LOCAL.getId())
			.orElseThrow(UserLoginInfoDifferentException::new)
			.toAccount();
		String refreshToken = getRefreshToken(account);
		return new SignInResponse(getAccessToken(account), refreshToken);
	}

	private String getRefreshToken(Account account) {
		String refreshToken = jwtTokenProvider.getRefreshToken(Map.of("id", account.getId()));
		refreshTokenRepository.save(new RefreshToken(account.getId(), refreshToken));
		return refreshToken;
	}

	private String getAccessToken(Account account) {
		return jwtTokenProvider.getAccessToken(Map.of("id", account.getId()));
	}

	public void signOut(long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}

	@Transactional(readOnly = true)
	public AccessTokenResponse getAccessToken(RefreshTokenRequest refreshTokenRequest) {
		if (!refreshTokenRepository.existsByToken(refreshTokenRequest.getToken())) {
			throw new AuthenticationException();
		}

		Account account = jwtTokenProvider.getAccount(refreshTokenRequest.getToken());
		return new AccessTokenResponse(getAccessToken(account));
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
