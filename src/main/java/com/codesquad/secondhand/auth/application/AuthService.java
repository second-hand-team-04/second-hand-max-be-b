package com.codesquad.secondhand.auth.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.AccessTokenResponse;
import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.domain.RefreshToken;
import com.codesquad.secondhand.auth.infrastrucure.AuthRepository;
import com.codesquad.secondhand.auth.infrastrucure.RefreshTokenRepository;
import com.codesquad.secondhand.common.exception.ErrorType;
import com.codesquad.secondhand.common.exception.auth.AuthUnauthorizedException;
import com.codesquad.secondhand.common.exception.user.UserLoginInfoDifferentException;
import com.codesquad.secondhand.common.util.AuthorizationHeaderUtil;

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
		return new SignInResponse(getAccessToken(account), getRefreshToken(account));
	}

	private String getRefreshToken(Account account) {
		String refreshToken = jwtTokenProvider.getRefreshToken(Map.of("id", account.getId()));
		refreshTokenRepository.save(new RefreshToken(account.getId(), refreshToken));
		return refreshToken;
	}

	private String getAccessToken(Account account) {
		return jwtTokenProvider.getAccessToken(Map.of("id", account.getId()));
	}

	/*
		public SignInResponse oauthSignIn(String providerName, String code) {
		// 1. code를 가지고 kakao에서 AccessToken 발급받고
		OauthClient oauthClient = OauthClientFactory.getOauthClient(providerName);
		String accessToken = oauthClient.getAcessToken(providerName, code);

		// 2. AccessToken으로 회원정보를 가져온다.
		OauthUserInfomation oauthUserInfomation = oauthClient.getUserInfomation(accessToken);

		// 3. kakao에서 가져온 회원정보로 회원이 있는지 확인 후
		Long providerId = ProviderType.findIdByName(providerName);
		User user = authRepository.findByEmailAndProviderId(oauthUserInfomation.getEmail(), providerId)
			.orElseGet(() -> userService.signUp(new UserCreateRequest(providerId, oauthUserInfomation.getEmail(),
				oauthUserInfomation.getNickname(), null), null));
		return new SignInResponse(getAccessToken(user.toAccount()), getRefreshToken(user.toAccount()));
	}*/

	public void signOut(long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}

	@Transactional(readOnly = true)
	public AccessTokenResponse getAccessToken(String authorizationHeader) {
		try {
			String refreshToken = AuthorizationHeaderUtil.getRefreshToken(authorizationHeader);
			Account account = jwtTokenProvider.getAccount(refreshToken);
			return new AccessTokenResponse(getAccessToken(account));
		} catch (RuntimeException e) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_REFRESH_TOKEN_UNAUTHORIZED);
		}
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
