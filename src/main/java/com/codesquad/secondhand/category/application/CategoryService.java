package com.codesquad.secondhand.category.application;

import static com.codesquad.secondhand.category.domain.Category.CATEGORY_ALL;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.CATEGORY;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
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

	@Cacheable(cacheNames = CATEGORY, key = "'all'")
	public List<CategoryResponse> findAll() {
		return CategoryResponse.from(categoryRepository.findAll());
	}

	public Category findByIdOrThrow(Long categoryId) {
		if (categoryId.equals(CATEGORY_ALL)) {
			throw new CategoryNotFoundException();
		}

		return categoryRepository.findById(categoryId)
			.orElseThrow(CategoryNotFoundException::new);
	}

	public List<Category> findCategoriesOnMyWishlistByUserId(Long userId) {
		return categoryRepository.findCategoriesOnMyWishlistByUserId(userId);
	}
}
