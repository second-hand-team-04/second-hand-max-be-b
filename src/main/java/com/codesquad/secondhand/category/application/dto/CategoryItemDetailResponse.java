package com.codesquad.secondhand.category.application.dto;

import com.codesquad.secondhand.category.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryItemDetailResponse {

	private Long id;
	private String title;

	public static CategoryItemDetailResponse from(Category category) {
		return new CategoryItemDetailResponse(category.getId(), category.getTitle());
	}
}
