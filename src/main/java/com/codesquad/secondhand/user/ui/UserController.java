package com.codesquad.secondhand.user.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.application.dto.UserRegionCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/regions")
	public ResponseEntity<CommonResponse> showMyRegions() {
		// TODO 로그인 한 유저 아이디 가져오기
		Long userId = 1L;

		CommonResponse commonResponse = CommonResponse.createOK(
			userService.findUserRegions(userId),
			ResponseMessage.MY_REGION_FIND_ALL);
		return ResponseEntity.ok().body(commonResponse);
	}

	@PostMapping("/regions")
	public ResponseEntity<CommonResponse> addMyRegions(@RequestBody UserRegionCreateRequest request) {
		// TODO 로그인 한 유저 아이디 가져오기
		Long userId = 1L;

		request.injectUserId(userId);
		userService.addRegions(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.MY_REGION_ADD));
	}
}

