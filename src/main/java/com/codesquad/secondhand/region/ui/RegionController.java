package com.codesquad.secondhand.region.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.region.application.RegionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;

	@GetMapping
	public ResponseEntity<CommonResponse> showRegions(@RequestParam int cursor) {
		CommonResponse commonResponse = CommonResponse.createOK(
			regionService.findAll(cursor),
			ResponseMessage.REGION_FIND_ALL
		);
		return ResponseEntity.ok().body(commonResponse);
	}
}
