package com.codesquad.secondhand.region.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.region.application.RegionFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

	private final RegionFacade regionFacade;

	@GetMapping
	public ResponseEntity<CommonResponse> showRegions(Pageable pageable,
		@RequestParam(defaultValue = "") String title) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				regionFacade.findAllByTitle(pageable, title),
				ResponseMessage.REGION_FIND_ALL));
	}
}
