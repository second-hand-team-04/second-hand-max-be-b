package com.codesquad.secondhand.item.application;

import static com.codesquad.secondhand.common.util.RedisUtil.ITEM;
import static com.codesquad.secondhand.common.util.RedisUtil.ITEM_VIEW_COUNT;
import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.common.exception.item.ItemNotFoundException;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusResponse;
import com.codesquad.secondhand.item.application.dto.MyTransactionResponse;
import com.codesquad.secondhand.item.application.dto.MyTransactionSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;
import com.codesquad.secondhand.item.infrastructure.dto.ItemDetailDto;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemSliceResponse findItemsByCategoryAndRegion(Long categoryId, Long regionId, Pageable pageable) {
		Slice<ItemResponse> itemSlice = itemRepository.findByCategoryIdAndRegionId(categoryId, regionId, pageable);
		return ItemSliceResponse.of(itemSlice.hasNext(), itemSlice.getContent());
	}

	@Cacheable(cacheNames = ITEM, key = "#id")
	public ItemDetailDto findDetailById(Long id) {
		return itemRepository.findDetailById(id)
			.orElseThrow(ItemNotFoundException::new);
	}

	public int incrementViewCount(Long id) {
		return itemRepository.incrementViewCount(id);
	}

	public Item findByIdOrElseThrow(Long id) {
		return itemRepository.findById(id)
			.orElseThrow(ItemNotFoundException::new);
	}

	@Transactional
	public Item create(Item item) {
		return itemRepository.save(item);
	}

	@Transactional
	@CacheEvict(cacheNames = ITEM, key = "#itemUpdateStatusRequest.id")
	public ItemUpdateStatusResponse updateStatus(ItemUpdateStatusRequest itemUpdateStatusRequest, User user, Status status) {
		Item item = findByIdOrElseThrow(itemUpdateStatusRequest.getId());
		item.updateStatus(user, status);
		return new ItemUpdateStatusResponse(itemUpdateStatusRequest.getId());
	}

	public MyTransactionSliceResponse findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable) {
		Slice<ItemResponse> itemSlice = itemRepository.findAllMyTransactionByStatus(userId, statusIds, pageable);
		return MyTransactionSliceResponse.of(itemSlice.hasNext(), MyTransactionResponse.from(itemSlice.getContent()));
	}

	@Transactional
	@CacheEvict(cacheNames = ITEM, key = "#request.id")
	public ItemUpdateResponse update(ItemUpdateRequest request, User accountUser, List<Image> images, Category category, Region region) {
		Item item = findByIdOrElseThrow(request.getId());
		item.update(request.getTitle(), request.getPrice(), request.getContent(), accountUser, images, category, region);
		return new ItemUpdateResponse(item.getId());
	}

	@Transactional
	@Caching(
		evict = {
			@CacheEvict(cacheNames = ITEM, key = "#id"),
			@CacheEvict(cacheNames = ITEM_VIEW_COUNT, key = "#id"),
			@CacheEvict(cacheNames = WISH_ITEM, key = "#id")
		}
	)
	public void delete(Long id, User user) {
		Item item = findByIdOrElseThrow(id);
		item.delete(user);
	}

	public Slice<ItemResponse> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable) {
		return itemRepository.findByUserIdAndCategoryId(userId, categoryId, pageable);
	}

	@Transactional
	@Scheduled(cron = "0 0 1 * * *")
	public void scheduledUpdateViews() {
		itemRepository.findAllRedisItemViewCount()
				.forEach(itemRepository::updateViewsByIdAndViews);
	}
}
