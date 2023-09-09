package com.codesquad.secondhand.item.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.application.ImageService;
import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.category.application.CategoryService;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.common.exception.item.ItemImageEmptyException;
import com.codesquad.secondhand.common.exception.item.StatusNotFoundException;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.domain.StatusType;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;
import com.codesquad.secondhand.item.infrastructure.StatusRepository;
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
	private final StatusRepository statusRepository;

	public ItemSliceResponse findItemsByCategoryAndRegion(Long categoryId, Long regionId, Pageable pageable) {
		Slice<Item> itemSlice = itemRepository.findByCategoryIdAndRegionId(categoryId, regionId, pageable);
		return ItemSliceResponse.of(itemSlice.hasNext(),
			ItemResponse.of(itemSlice.getContent()));
	}

	@Transactional
	public void create(Long userId, ItemCreateRequest request) {
		//FIXME 이미지 id 중복 허용?
		//FIXME 이미지 DB에서 조회하기 전에 이미지 갯수부터 검증
		List<Image> images = request.getImageIds().stream()
			.map(imageService::findByIdOrThrow)
			.collect(Collectors.toUnmodifiableList());
		Category category = categoryService.findByIdOrThrow(request.getCategoryId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		User user = userService.findByIdOrThrow(userId);

		Item item = request.toItem(images, category, region);

		item.updateUser(user);
		item.updateStatus(statusRepository.findById(StatusType.FOR_SALE.getId())
			.orElseThrow(StatusNotFoundException::new));

		itemRepository.save(item);
	}
}
