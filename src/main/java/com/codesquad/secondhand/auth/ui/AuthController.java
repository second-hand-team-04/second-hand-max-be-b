package com.codesquad.secondhand.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.auth.application.AuthService;
import com.codesquad.secondhand.auth.application.dto.RefreshTokenRequest;
import com.codesquad.secondhand.auth.application.dto.SignInRequest;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping
	public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(authService.signIn(signInRequest), ResponseMessage.SIGN_IN));
	}

	@PostMapping("/access-token")
	public ResponseEntity<CommonResponse> showAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(authService.getAccessToken(refreshTokenRequest), ResponseMessage.SIGN_OUT));
	}
}
