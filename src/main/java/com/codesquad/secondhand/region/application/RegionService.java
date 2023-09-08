package com.codesquad.secondhand.region.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.region.RegionNotFoundException;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.application.dto.RegionSliceResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.region.infrastructure.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionService {

	private final RegionRepository regionRepository;

	public RegionSliceResponse findAllByTitle(Pageable pageable, String title) {
		Slice<Region> regionSlice = regionRepository.findSliceByTitleContainsOrderByTitle(pageable, title);
		return RegionSliceResponse.of(regionSlice.hasNext(), RegionResponse.from(regionSlice.getContent()));
	}

	public Region findByIdOrThrow(Long id) {
		return regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
	}
}
