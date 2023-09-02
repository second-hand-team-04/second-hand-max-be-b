package com.codesquad.secondhand.item.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.application.ImageService;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemSliceResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.infrastructure.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ImageService imageService;

	public ItemSliceResponse findItemsByCategoryAndRegion(Long categoryId, Long regionId, Pageable pageable) {
		Slice<Item> itemSlice = itemRepository.findByCategoryIdAndRegionId(categoryId, regionId, pageable);
		return ItemSliceResponse.of(itemSlice.hasNext(),
			ItemResponse.of(itemSlice.getContent(), imageService.getItemDefaultImage()));
	}
}
