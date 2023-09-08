package com.codesquad.secondhand.category.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.category.application.dto.CategoryResponse;
import com.codesquad.secondhand.category.infrastructure.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public List<CategoryResponse> findAll() {
		return CategoryResponse.from(categoryRepository.findAll());
	}
}
