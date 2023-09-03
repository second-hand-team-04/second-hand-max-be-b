package com.codesquad.secondhand.auth.application;

import org.springframework.stereotype.Service;

import com.codesquad.secondhand.auth.application.dto.AccessTokenResponse;
import com.codesquad.secondhand.auth.application.dto.RefreshTokenRequest;
import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.infrastrucure.AuthRepository;
import com.codesquad.secondhand.auth.infrastrucure.RefreshTokenRepository;
import com.codesquad.secondhand.common.exception.auth.AuthenticationException;
import com.codesquad.secondhand.common.exception.user.UserLoginInfoDifferentException;

import lombok.RequiredArgsConstructor;

@Service
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

		String refreshToken = jwtTokenProvider.refreshToken(account);
		refreshTokenRepository.save(account.getId(), refreshToken);
		return new SignInResponse(jwtTokenProvider.accessToken(account), refreshToken);
	}

	public void signOut(long userId) {
		refreshTokenRepository.remove(userId);
	}

	public AccessTokenResponse getAccessToken(RefreshTokenRequest refreshTokenRequest) {
		Account account = jwtTokenProvider.getAccount(refreshTokenRequest.getRefreshToken());

		if (!refreshTokenRepository.existsByUserId(account.getId())) {
			throw new AuthenticationException();
		}

		return new AccessTokenResponse(jwtTokenProvider.accessToken(account));
	}
}
