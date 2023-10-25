package com.codesquad.secondhand.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.domain.OauthUserInfomation;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.infrastrucure.AuthRepository;
import com.codesquad.secondhand.auth.infrastrucure.oauth.OauthClient;
import com.codesquad.secondhand.auth.infrastrucure.oauth.dto.OauthTokenResponse;
import com.codesquad.secondhand.common.exception.user.UserEmailAndProviderDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserLoginInfoDifferentException;
import com.codesquad.secondhand.common.exception.user.UserNicknameDuplicationException;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;
	private final OauthClient oauthClient;

	@Transactional(readOnly = true)
	public User findByEmailAndPasswordProviderId(SignInRequest signInRequest) {
		return authRepository.findByEmailAndPasswordAndProviderId(signInRequest.getEmail(),
				signInRequest.getPassword(), ProviderType.LOCAL.getId())
			.orElseThrow(UserLoginInfoDifferentException::new);
	}

	public OauthUserInfomation findOauthUserInfomation(ProviderType providerType, String code) {
		OauthTokenResponse oauthTokenResponse = oauthClient.generateAccessToken(providerType, code);
		return oauthClient.generateUserInfomation(providerType, oauthTokenResponse.getAccessToken());
	}
	
	public User oauthSignIn(OauthUserInfomation oauthUserInfomation, Provider provider, Region region) {
		return authRepository.findByEmailAndProviderId(oauthUserInfomation.getEmail(), provider.getId())
			.orElseGet(() -> {
				UserCreateRequest request = new UserCreateRequest(provider.getId(),
					oauthUserInfomation.getEmail(), oauthUserInfomation.getNickname(), null);
				validateDuplication(request, provider.getId());
				User user = authRepository.save(request.toUser(provider, null, region));
				user.addMyRegion(region);
				return user;
			});
	}

	private void validateDuplication(UserCreateRequest request, Long providerId) {
		if (authRepository.existsByEmailAndProviderId(request.getEmail(), providerId)) {
			throw new UserEmailAndProviderDuplicationException();
		}

		if (authRepository.existsByNickname(request.getNickname())) {
			throw new UserNicknameDuplicationException();
		}
	}
}
