package com.codesquad.secondhand.user.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.image.application.ImageService;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.application.ItemService;
import com.codesquad.secondhand.item.application.dto.MyTransactionSliceResponse;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserInfoResponse;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;
import com.codesquad.secondhand.user.domain.Provider;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final ProviderService providerService;
	private final RegionService regionService;
	private final ImageService imageService;
	private final ItemService itemService;

	public void signUp(UserCreateRequest request, MultipartFile profileImage) {
		Provider provider = providerService.findByIdOrElseThrow(request.getProviderId());
		Image image = imageService.uploadOrElseNull(profileImage);
		Region region = regionService.findByIdOrThrow(Region.DEFAULT_REGION_YEOKSAM_DONG);
		userService.signUp(request, provider, image, region);
	}

	public void updateProfile(UserUpdateRequest request, MultipartFile profileImage) {
		Image image = imageService.uploadOrElseNull(profileImage);
		userService.updateProfile(request, image);
	}

	@Transactional(readOnly = true)
	public UserInfoResponse findByIdOrThrow(Long id) {
		return UserInfoResponse.from(userService.findByIdOrThrow(id));
	}

	@Transactional(readOnly = true)
	public List<RegionResponse> findUserRegions(Long id) {
		return userService.findUserRegions(id);
	}

	public void addMyRegion(UserRegionAddRequest request) {
		Region region = regionService.findByIdOrThrow(request.getId());
		userService.addMyRegion(request, region);
	}

	public void removeMyRegion(Long userId, Long regionId) {
		Region region = regionService.findByIdOrThrow(regionId);
		userService.removeMyRegion(userId, region);
	}

	@Transactional(readOnly = true)
	public MyTransactionSliceResponse findAllMyTransactionByStatus(Long id, List<Long> status, Pageable pageable) {
		return itemService.findAllMyTransactionByStatus(id, status, pageable);
	}
}
