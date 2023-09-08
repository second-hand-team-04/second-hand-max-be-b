package com.codesquad.secondhand.item.ui;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.item.application.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping
	public ResponseEntity<CommonResponse> showHome(Pageable pageable,
		@RequestParam Long category, @RequestParam Long region) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemService.findItemsByCategoryAndRegion(category, region, pageable),
				ResponseMessage.ITEM_FIND_BY_REGION_AND_CATEGORY));
	}
}

