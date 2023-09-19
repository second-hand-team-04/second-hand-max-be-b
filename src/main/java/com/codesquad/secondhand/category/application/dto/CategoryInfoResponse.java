package com.codesquad.secondhand.category.application.dto;

import com.codesquad.secondhand.category.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryInfoResponse {

	private Long id;
	private String title;

	public static CategoryInfoResponse from(Category category) {
		return new CategoryInfoResponse(category.getId(), category.getTitle());
	}
}
