package com.codesquad.secondhand.item.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.application.ImageService;
import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.category.application.CategoryService;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.common.exception.item.ItemNotFoundException;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.domain.StatusType;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final RegionService regionService;
	private final UserService userService;
	private final StatusService statusService;

	public ItemSliceResponse findItemsByCategoryAndRegion(Long categoryId, Long regionId, Pageable pageable) {
		Slice<Item> itemSlice = itemRepository.findByCategoryIdAndRegionId(categoryId, regionId, pageable);
		return ItemSliceResponse.of(itemSlice.hasNext(),
			ItemResponse.of(itemSlice.getContent()));
	}

	@Transactional
	public ItemDetailResponse findById(Long id, Account account) {
		Item item = itemRepository.findDetailById(id)
			.orElseThrow(ItemNotFoundException::new);
		item.increaseViewCount();
		return ItemDetailResponse.from(item, account);
	}

	@Transactional
	public void create(Long userId, ItemCreateRequest request) {
		List<Image> images = imageService.findAllByIdOrThrow(request.getImageIds());
		Category category = categoryService.findByIdOrThrow(request.getCategoryId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		Status status = statusService.findByIdOrThrow(StatusType.FOR_SALE.getId());
		User user = userService.findByIdOrThrow(userId);

		Item item = request.toItem(images, category, region, status, user);

		itemRepository.save(item);
	}
}
