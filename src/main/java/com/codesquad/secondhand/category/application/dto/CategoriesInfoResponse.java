package com.codesquad.secondhand.category.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoriesInfoResponse {

	private List<CategoryInfoResponse> categories;
}
