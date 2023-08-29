package com.codesquad.secondhand.item.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.common.domain.Cursor;
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
	public ResponseEntity<CommonResponse> showHome(@RequestParam(defaultValue = "0") Cursor cursor,
		@RequestParam Long category, @RequestParam Long region) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemService.findItemsByCategoryAndRegion(cursor.getValue(), category, region),
				ResponseMessage.REGION_FIND_ALL));
	}
}

