package com.codesquad.secondhand.region.application;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.application.dto.RegionSliceResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.region.infrastructure.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionService {

	private static final int MAX_REGION_SLICE_SIZE = 20;

	private final RegionRepository regionRepository;

	public RegionSliceResponse findAll(int cursor) {
		Pageable pageable = PageRequest.of(cursor, MAX_REGION_SLICE_SIZE, Sort.by(Order.asc("title")));
		Slice<Region> regionSlice = regionRepository.findSliceBy(pageable);
		return RegionSliceResponse.of(regionSlice.hasNext(), RegionResponse.from(regionSlice.getContent()));
	}
}
