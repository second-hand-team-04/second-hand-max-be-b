package com.codesquad.secondhand.category.application.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.category.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryInfoResponse implements Serializable {

	private Long id;
	private String title;

	public static CategoryInfoResponse from(Category category) {
		return new CategoryInfoResponse(category.getId(), category.getTitle());
	}

	public static List<CategoryInfoResponse> from(List<Category> categories) {
		return categories.stream()
			.map(CategoryInfoResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
