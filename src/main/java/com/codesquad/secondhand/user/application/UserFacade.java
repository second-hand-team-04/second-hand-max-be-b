package com.codesquad.secondhand.user.application;

import static com.codesquad.secondhand.common.util.CacheType.CacheName.MY_REGION;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.category.application.CategoryService;
import com.codesquad.secondhand.category.application.dto.CategoriesInfoResponse;
import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.image.application.ImageService;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.application.ItemService;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.MyTransactionSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.MyRegionResponse;
import com.codesquad.secondhand.user.application.dto.MyWishlistResponse;
import com.codesquad.secondhand.user.application.dto.MyWishlistSliceResponse;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserInfoResponse;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

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
	private final CategoryService categoryService;

	public void signUp(UserCreateRequest request, MultipartFile profileImage) {
		Provider provider = providerService.findByIdOrElseThrow(request.getProviderId());
		Image image = imageService.uploadOrElseNull(profileImage);
		Region region = regionService.findByIdOrThrow(Region.DEFAULT_REGION);
		userService.signUp(request, provider, image, region);
	}

	public void updateProfile(UserUpdateRequest request, MultipartFile profileImage) {
		Image image = imageService.uploadOrElseNull(profileImage);
		userService.updateProfile(request, image);
	}

	@Transactional(readOnly = true)
	public UserInfoResponse findById(Long id) {
		return UserInfoResponse.from(userService.findByIdOrThrow(id));
	}

	@Transactional(readOnly = true)
	public MyRegionResponse findMyRegions(Long id) {
		return userService.findUserRegions(id);
	}

	public void addMyRegion(UserRegionAddRequest request, Long id) {
		Region region = regionService.findByIdOrThrow(request.getId());
		userService.addMyRegion(id, region);
	}

	@CacheEvict(cacheNames = MY_REGION, key = "#userId")
	public void updateSelectedMyRegion(Long userId, Long regionId) {
		User user = userService.findByIdOrThrow(userId);
		Region region = regionService.findByIdOrThrow(regionId);
		user.updateSelectedRegion(region);
	}

	public void removeMyRegion(Long userId, Long regionId) {
		Region region = regionService.findByIdOrThrow(regionId);
		userService.removeMyRegion(userId, region);
	}

	@Transactional(readOnly = true)
	public MyTransactionSliceResponse findAllMyTransactionByStatus(Long id, List<Long> status, Pageable pageable) {
		return itemService.findAllMyTransactionByStatus(id, status, pageable);
	}

	public void addMyWishlist(Long id, Long itemId) {
		Item item = itemService.findByIdOrElseThrow(itemId);
		userService.addMyWishlist(id, item);
	}

	public void removeMyWishlist(Long id, Long itemId) {
		Item item = itemService.findByIdOrElseThrow(itemId);
		userService.removeMyWishlist(id, item);
	}

	@Transactional(readOnly = true)
	public MyWishlistSliceResponse findMyWishlistByCategory(Long id, Long categoryId, Pageable pageable) {
		Slice<ItemResponse> itemSlice = itemService.findByUserIdAndCategoryId(id, categoryId, pageable);
		return MyWishlistSliceResponse.of(itemSlice.hasNext(), MyWishlistResponse.from(itemSlice.getContent()));
	}

	@Transactional(readOnly = true)
	public CategoriesInfoResponse findCategoriesOnMyWishlist(Long id) {
		List<CategoryInfoResponse> categoryInfoResponses = CategoryInfoResponse.from(
			categoryService.findCategoriesOnMyWishlistByUserId(id));
		return new CategoriesInfoResponse(categoryInfoResponses);
	}
}
