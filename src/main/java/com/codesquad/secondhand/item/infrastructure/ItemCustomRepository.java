package com.codesquad.secondhand.item.infrastructure;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemCustomRepository {

	Slice<Item> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable);

	Slice<Item> findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable);

	Slice<Item> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);
}
