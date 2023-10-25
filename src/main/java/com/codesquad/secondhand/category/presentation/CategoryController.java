package com.codesquad.secondhand.category.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.category.application.CategoryFacade;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryFacade categoryFacade;

	@GetMapping
	public ResponseEntity<CommonResponse> showCategories() {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				categoryFacade.findAll(),
				ResponseMessage.CATEGORY_FIND_ALL));
	}
}
