package com.codesquad.secondhand.region.application;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.region.application.dto.RegionSliceResponse;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class RegionFacade {

	private final RegionService regionService;

	@Transactional(readOnly = true)
	public RegionSliceResponse findAllByTitle(Pageable pageable, String title) {
		return regionService.findAllByTitle(pageable, title);
	}
}
