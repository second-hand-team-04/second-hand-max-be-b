package com.codesquad.secondhand.category.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.category.application.dto.CategoryResponse;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.category.infrastructure.CategoryRepository;
import com.codesquad.secondhand.common.exception.category.CategoryNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public List<CategoryResponse> findAll() {
		return CategoryResponse.from(categoryRepository.findAll());
	}

	public Category findByIdOrThrow(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
	}
}
