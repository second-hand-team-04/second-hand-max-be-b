package com.codesquad.secondhand.user.ui;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserInfoResponse;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<CommonResponse> signUp(@RequestPart @Valid UserCreateRequest request,
		@RequestPart(required = false) MultipartFile image) {
		request.injectProviderId(ProviderType.LOCAL.getId());
		userService.signUp(request, image);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.SIGN_UP));
	}

	@PatchMapping("/info")
	public ResponseEntity<CommonResponse> updateProfile(@RequestPart @Valid UserUpdateRequest request,
		@RequestPart(required = false) MultipartFile image, @AccountPrincipal Account account) {
		request.injectId(account.getId());
		userService.updateProfile(request, image);
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.UPDATE_PROFILE));
	}

	@GetMapping("/regions")
	public ResponseEntity<CommonResponse> showMyRegions(@AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				userService.findUserRegions(account.getId()),
				ResponseMessage.MY_REGION_FIND_ALL));
	}

	@PostMapping("/regions")
	public ResponseEntity<CommonResponse> addMyRegion(@RequestBody UserRegionAddRequest request,
		@AccountPrincipal Account account) {
		request.injectUserId(account.getId());
		userService.addMyRegion(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.MY_REGION_ADD));
	}

	@DeleteMapping("/regions/{id}")
	public ResponseEntity<CommonResponse> removeMyRegion(@PathVariable Long id,
		@AccountPrincipal Account account) {
		userService.removeMyRegion(account.getId(), id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(CommonResponse.createNoContent(ResponseMessage.MY_REGION_REMOVE));
	}

	@GetMapping("/info")
	public ResponseEntity<CommonResponse> showUserInfo(@AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				UserInfoResponse.from(userService.findByIdOrThrow(account.getId())),
				ResponseMessage.USER_INFO
			));
	}
}

