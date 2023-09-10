package com.codesquad.secondhand.category.application.dto;

import com.codesquad.secondhand.category.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryItemDetailResponse {

	private String category;

	public static CategoryItemDetailResponse from(Category category) {
		return new CategoryItemDetailResponse(category.getTitle());
	}
}
