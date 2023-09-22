package com.codesquad.secondhand.category.application.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.category.domain.Category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CategoryResponse implements Serializable {

	private Long id;
	private String title;
	private String imageUrl;

	public static CategoryResponse from(Category category) {
		return new CategoryResponse(
			category.getId(),
			category.getTitle(),
			category.getImageUrl()
		);
	}

	public static List<CategoryResponse> from(List<Category> categories) {
		return categories.stream()
			.map(CategoryResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
