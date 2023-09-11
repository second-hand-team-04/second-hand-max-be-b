package com.codesquad.secondhand.auth.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.application.dto.SignInResponse;
import com.codesquad.secondhand.auth.domain.OauthUserInfomation;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.ProviderService;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class AuthFacade {

	private final AuthService authService;
	private final TokenService tokenService;
	private final ProviderService providerService;
	private final RegionService regionService;

	public SignInResponse signIn(SignInRequest signInRequest) {
		User user = authService.findByEmailAndPasswordProviderId(signInRequest);
		return tokenService.createToken(user.toAccount());
	}

	public SignInResponse oauthSignIn(String providerName, String code) {
		ProviderType providerType = ProviderType.findByName(providerName);
		OauthUserInfomation oauthUserInfomation = authService.findOauthUserInfomation(providerType, code);
		Provider provider = providerService.findByIdOrElseThrow(providerType.getId());
		Region region = regionService.findByIdOrThrow(Region.DEFAULT_REGION_YEOKSAM_DONG);
		User user = authService.oauthSignIn(oauthUserInfomation, provider, region);
		return tokenService.createToken(user.toAccount());
	}

	public void signOut(long userId) {
		tokenService.deleteRefreshToken(userId);
	}
}
