package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemCustomRepository {

	Slice<Item> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable);
}
