package com.codesquad.secondhand.user.presentation;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.user.application.UserFacade;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserFacade userFacade;

	@PostMapping
	public ResponseEntity<CommonResponse> signUp(@RequestPart @Valid UserCreateRequest request,
		@RequestPart(required = false) MultipartFile image) {
		request.injectProviderId(ProviderType.LOCAL.getId());
		userFacade.signUp(request, image);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.SIGN_UP));
	}

	@PatchMapping("/info")
	public ResponseEntity<CommonResponse> updateProfile(@RequestPart @Valid UserUpdateRequest request,
		@RequestPart(required = false) MultipartFile image, @AccountPrincipal Account account) {
		request.injectId(account.getId());
		userFacade.updateProfile(request, image);
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.UPDATE_PROFILE));
	}

	@GetMapping("/regions")
	public ResponseEntity<CommonResponse> showMyRegions(@AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				userFacade.findMyRegions(account.getId()),
				ResponseMessage.MY_REGION_FIND_ALL));
	}

	@PostMapping("/regions")
	public ResponseEntity<CommonResponse> addMyRegion(@RequestBody UserRegionAddRequest request,
		@AccountPrincipal Account account) {
		request.injectUserId(account.getId());
		userFacade.addMyRegion(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.MY_REGION_ADD));
	}

	@PatchMapping("/regions/{id}")
	public ResponseEntity<CommonResponse> updateSelectedMyRegion(@PathVariable Long id,
		@AccountPrincipal Account account) {
		userFacade.updateSelectedMyRegion(account.getId(), id);
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.MY_REGION_SELECTED));
	}

	@DeleteMapping("/regions/{id}")
	public ResponseEntity<CommonResponse> removeMyRegion(@PathVariable Long id,
		@AccountPrincipal Account account) {
		userFacade.removeMyRegion(account.getId(), id);
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.MY_REGION_REMOVE));
	}

	@GetMapping("/info")
	public ResponseEntity<CommonResponse> showUserInfo(@AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				userFacade.findById(account.getId()),
				ResponseMessage.USER_INFO
			));
	}

	@GetMapping("/transactions")
	public ResponseEntity<CommonResponse> showMySalesByStatus(@AccountPrincipal Account account,
		@RequestParam(required = false) List<Long> status, Pageable pageable) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				userFacade.findAllMyTransactionByStatus(account.getId(), status, pageable),
				ResponseMessage.USER_FIND_MY_TRANSACTION_BY_STATUS
			));
	}

	@GetMapping("/wishlist")
	public ResponseEntity<CommonResponse> showMyWishlists(@AccountPrincipal Account account,
		@RequestParam(required = false) Long categoryId, Pageable pageable) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				userFacade.findMyWishlistByCategory(account.getId(), categoryId, pageable),
				ResponseMessage.USER_WISHLIST_FIND
			));
	}

	@PostMapping("/wishlist/{itemId}")
	public ResponseEntity<CommonResponse> addWishlist(@AccountPrincipal Account account, @PathVariable Long itemId) {
		userFacade.addMyWishlist(account.getId(), itemId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.USER_WISHLIST_ADD));
	}

	@DeleteMapping("/wishlist/{itemId}")
	public ResponseEntity<CommonResponse> deleteWishlist(@AccountPrincipal Account account, @PathVariable Long itemId) {
		userFacade.removeMyWishlist(account.getId(), itemId);
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.USER_WISHLIST_REMOVE));
	}
}

