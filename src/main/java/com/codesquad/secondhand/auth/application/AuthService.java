package com.codesquad.secondhand.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.infrastrucure.AuthRepository;
import com.codesquad.secondhand.auth.infrastrucure.oauth.OauthClient;
import com.codesquad.secondhand.auth.domain.OauthUserInfomation;
import com.codesquad.secondhand.auth.infrastrucure.oauth.dto.OauthTokenResponse;
import com.codesquad.secondhand.common.exception.user.UserLoginInfoDifferentException;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;
	private final TokenService tokenService;
	private final UserService userService;
	private final OauthClient oauthClient;

	public SignInResponse signIn(SignInRequest signInRequest) {
		Account account = authRepository.findByEmailAndPasswordAndProviderId(signInRequest.getEmail(),
				signInRequest.getPassword(), ProviderType.LOCAL.getId())
			.orElseThrow(UserLoginInfoDifferentException::new)
			.toAccount();
		return tokenService.createToken(account);
	}

	public SignInResponse oauthSignIn(String providerName, String code) {
		ProviderType providerType = ProviderType.findByName(providerName);
		OauthTokenResponse oauthTokenResponse = oauthClient.generateAccessToken(providerType, code);
		OauthUserInfomation oauthUserInfomation = oauthClient.generateUserInfomation(providerType, oauthTokenResponse.getAccessToken());
		User user = authRepository.findByEmailAndProviderId(oauthUserInfomation.getEmail(), providerType.getId())
			.orElseGet(() -> userService.signUp(new UserCreateRequest(providerType.getId(), oauthUserInfomation.getEmail(),
				oauthUserInfomation.getNickname(), null), null));
		return tokenService.createToken(user.toAccount());
	}

	public void signOut(long userId) {
		tokenService.deleteRefreshToken(userId);
	}
}
