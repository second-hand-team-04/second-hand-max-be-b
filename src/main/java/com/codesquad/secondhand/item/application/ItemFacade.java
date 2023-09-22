package com.codesquad.secondhand.item.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.category.application.CategoryService;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.image.application.ImageService;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemCreateResponse;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.domain.StatusType;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;
import com.codesquad.secondhand.item.infrastructure.dto.ItemDetailDto;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class ItemFacade {

	private final ItemService itemService;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final RegionService regionService;
	private final StatusService statusService;
	private final UserService userService;
	private final ItemImageService itemImageService;

	@Transactional(readOnly = true)
	public ItemSliceResponse findItemsByCategoryAndRegion(Long category, Long region, Pageable pageable) {
		return itemService.findItemsByCategoryAndRegion(category, region, pageable);
	}

	public ItemDetailResponse findDetailById(Long id, Long userId) {
		ItemDetailDto itemDetailDto = itemService.findDetailById(id);
		int itemViewCount = itemService.incrementViewCount(id);
		WishItem wishItem = userService.findWishItem(id);
		return ItemDetailResponse.of(itemDetailDto, itemViewCount, wishItem, userId);
	}

	public ItemCreateResponse create(ItemCreateRequest request, Long userId) {
		List<Image> images = imageService.findAllByIdOrThrow(request.getImageIds());
		Category category = categoryService.findByIdOrThrow(request.getCategoryId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		Status status = statusService.findByIdOrThrow(StatusType.FOR_SALE.getId());
		User user = userService.findByIdOrThrow(userId);
		Item item = itemService.create(request.toItem(images, category, region, status, user));
		return new ItemCreateResponse(item.getId());
	}

	public ItemUpdateStatusResponse updateStatus(ItemUpdateStatusRequest request) {
		User user = userService.findByIdOrThrow(request.getUserId());
		Status status = statusService.findByIdOrThrow(request.getStatus());
		return itemService.updateStatus(request, user, status);
	}

	public ItemUpdateResponse update(ItemUpdateRequest request) {
		List<Image> images = imageService.findAllByIdOrThrow(request.getImageIds());
		Category category = categoryService.findByIdOrThrow(request.getCategoryId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		User user = userService.findByIdOrThrow(request.getUserId());
		return itemService.update(request, user, images, category, region);
	}

	public void delete(Long id, Long userId) {
		User user = userService.findByIdOrThrow(userId);
		itemService.delete(id, user);
		itemImageService.deleteByItemId(id);
	}
}
