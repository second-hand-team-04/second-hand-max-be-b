package com.codesquad.secondhand.item.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.infrastructure.dto.ItemDetailDto;

public interface ItemCustomRepository {

	Slice<ItemResponse> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable);

	Slice<ItemResponse> findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable);

	Slice<ItemResponse> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);

	Optional<ItemDetailDto> findDetailById(Long id);

	int incrementViewCount(Long id);

	Map<Long, Integer> findAllRedisItemViewCount();
}
