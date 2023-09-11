package com.codesquad.secondhand.item.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.application.ImageService;
import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.category.application.CategoryService;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusResponse;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.domain.StatusType;
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

	@Transactional(readOnly = true)
	public ItemSliceResponse findItemsByCategoryAndRegion(Long category, Long region, Pageable pageable) {
		return itemService.findItemsByCategoryAndRegion(category, region, pageable);
	}

	public ItemDetailResponse findDetailById(Long id, Account account) {
		return itemService.findDetailById(id, account);
	}

	public void create(ItemCreateRequest request) {
		List<Image> images = imageService.findAllByIdOrThrow(request.getImageIds());
		Category category = categoryService.findByIdOrThrow(request.getCategoryId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		Status status = statusService.findByIdOrThrow(StatusType.FOR_SALE.getId());
		User user = userService.findByIdOrThrow(request.getUserId());
		itemService.create(request.toItem(images, category, region, status, user));
	}

	public ItemUpdateStatusResponse updateStatus(ItemUpdateStatusRequest itemUpdateStatusRequest) {
		Status status = statusService.findByIdOrThrow(itemUpdateStatusRequest.getStatus());
		return itemService.updateStatus(itemUpdateStatusRequest, status);
	}

	public void delete(Long id, Long userId) {
		itemService.delete(id, userId);
	}
}

