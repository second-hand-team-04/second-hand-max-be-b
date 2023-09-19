package com.codesquad.secondhand.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.auth.application.AuthFacade;
import com.codesquad.secondhand.auth.application.TokenService;
import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthFacade authFacade;
	private final TokenService tokenService;

	@PostMapping
	public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(authFacade.signIn(signInRequest), ResponseMessage.SIGN_IN));
	}

	@DeleteMapping
	public ResponseEntity<CommonResponse> signOut(@AccountPrincipal Account account) {
		authFacade.signOut(account.getId());
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.SIGN_OUT));
	}

	@GetMapping("/oauth/{provider-name}")
	public ResponseEntity<CommonResponse> oauthSignIn(@PathVariable("provider-name") String providerName,
		@RequestParam String code) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(authFacade.oauthSignIn(providerName, code), ResponseMessage.SIGN_IN));
	}

	@PostMapping("/refresh")
	public ResponseEntity<CommonResponse> showAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(tokenService.generateAccessToken(authorizationHeader),
				ResponseMessage.ACCESS_TOKEN));
	}
}
