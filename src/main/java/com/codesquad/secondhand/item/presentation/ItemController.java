package com.codesquad.secondhand.item.presentation;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;
import com.codesquad.secondhand.item.application.ItemFacade;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemFacade itemFacade;

	@GetMapping
	public ResponseEntity<CommonResponse> showHome(Pageable pageable,
		@RequestParam Long category, @RequestParam Long region) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemFacade.findItemsByCategoryAndRegion(category, region, pageable),
				ResponseMessage.ITEM_FIND_BY_REGION_AND_CATEGORY));
	}

	@PostMapping
	public ResponseEntity<CommonResponse> create(@AccountPrincipal Account account,
		@Valid @RequestBody ItemCreateRequest request) {
		request.injectUserId(account.getId());
		itemFacade.create(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CommonResponse.createCreated(ResponseMessage.ITEM_CREATE));
	}

	@GetMapping("{id}")
	public ResponseEntity<CommonResponse> showItemDetail(@PathVariable Long id, @AccountPrincipal Account account) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemFacade.findDetailById(id, account),
				ResponseMessage.ITEM_DETAIL_FIND));
	}

	@PatchMapping("{id}")
	public ResponseEntity<CommonResponse> updateStatus(@RequestBody ItemUpdateStatusRequest itemUpdateStatusRequest,
		@PathVariable Long id, @AccountPrincipal Account account) {
		itemUpdateStatusRequest.injectIdAndUserId(id, account.getId());
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemFacade.updateStatus(itemUpdateStatusRequest),
				ResponseMessage.ITEM_UPDATE_STATUS));
	}

	@PutMapping("{id}")
	public ResponseEntity<CommonResponse> update(@RequestBody ItemUpdateRequest itemUpdateRequest,
		@PathVariable Long id, @AccountPrincipal Account account) {
		itemUpdateRequest.injectIdAndUserId(id, account.getId());
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				itemFacade.update(itemUpdateRequest),
				ResponseMessage.ITEM_UPDATE));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> delete(@PathVariable Long id, @AccountPrincipal Account account) {
		itemFacade.delete(id, account.getId());
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(ResponseMessage.ITEM_DELETE));
	}
}

