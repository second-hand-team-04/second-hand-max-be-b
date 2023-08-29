package com.codesquad.secondhand.item.application;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private static final int MAX_ITEM_SLICE_SIZE = 20;

	private final ItemRepository itemRepository;

	public ItemSliceResponse findItemsByCategoryAndRegion(int cursor, Long categoryId, Long regionId) {
		Pageable pageable = PageRequest.of(cursor, MAX_ITEM_SLICE_SIZE, Sort.by(Sort.Order.asc("title")));
		Slice<Item> itemSlice = itemRepository.findSliceByCategoryIdAndRegionId(pageable, categoryId, regionId);
		return ItemSliceResponse.of(itemSlice.hasNext(), ItemResponse.from(itemSlice.getContent()));
	}
}
