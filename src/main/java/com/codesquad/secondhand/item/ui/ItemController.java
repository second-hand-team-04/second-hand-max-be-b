package com.codesquad.secondhand.item.ui;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.item.application.ItemService;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;

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

	@GetMapping("{id}")
	public ResponseEntity<CommonResponse> showItemDetail(@PathVariable Long id, @AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemService.findById(id, account),
				ResponseMessage.ITEM_DETAIL_FIND));
	}

	@PostMapping
	public ResponseEntity<CommonResponse> create(@AccountPrincipal Account account, @Valid @RequestBody ItemCreateRequest request) {
		itemService.create(account.getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.ITEM_CREATE));
	}
}

