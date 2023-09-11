package com.codesquad.secondhand.item.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.common.exception.item.ItemNotFoundException;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
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
import com.codesquad.secondhand.item.infrastructure.ItemImageRepository;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;
import com.codesquad.secondhand.region.domain.Region;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemSliceResponse findItemsByCategoryAndRegion(Long categoryId, Long regionId, Pageable pageable) {
		Slice<Item> itemSlice = itemRepository.findByCategoryIdAndRegionId(categoryId, regionId, pageable);
		return ItemSliceResponse.of(itemSlice.hasNext(),
			ItemResponse.of(itemSlice.getContent()));
	}

	@Transactional
	public ItemDetailResponse findDetailById(Long id, Account account) {
		Item item = itemRepository.findDetailById(id)
			.orElseThrow(ItemNotFoundException::new);
		item.increaseViewCount();
		return ItemDetailResponse.from(item, account);
	}

	public Item findByIdOrElseThrow(Long id) {
		return itemRepository.findById(id)
			.orElseThrow(ItemNotFoundException::new);
	}

	@Transactional
	public void create(Item item) {
		item.validateUpdateRegion(item.getRegion());
		itemRepository.save(item);
	}

	@Transactional
	public ItemUpdateStatusResponse updateStatus(ItemUpdateStatusRequest itemUpdateStatusRequest, Status status) {
		Item item = findByIdOrElseThrow(itemUpdateStatusRequest.getId());
		item.updateStatus(itemUpdateStatusRequest.getUserId(), status);
		return new ItemUpdateStatusResponse(itemUpdateStatusRequest.getId());
	}

	public MyTransactionSliceResponse findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable) {
		Slice<Item> itemSlice = itemRepository.findAllMyTransactionByStatus(userId, statusIds, pageable);
		return MyTransactionSliceResponse.of(itemSlice.hasNext(), MyTransactionResponse.of(itemSlice.getContent()));
	}

	@Transactional
	public ItemUpdateResponse update(ItemUpdateRequest request, List<Image> images, Category category, Region region) {
		Item item = findByIdOrElseThrow(request.getId());
		item.update(request.getTitle(), request.getPrice(), request.getContent(), request.getUserId(), images, category, region);
		return new ItemUpdateResponse(item.getId());
	}

	@Transactional
	public void delete(Long id, Long userId) {
		Item item = findByIdOrElseThrow(id);
		item.delete(userId);
	}
}
