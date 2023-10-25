package com.codesquad.secondhand.category.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.category.application.dto.CategoryResponse;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoryFacade {

	private final CategoryService categoryService;

	public List<CategoryResponse> findAll() {
		return categoryService.findAll();
	}
}
