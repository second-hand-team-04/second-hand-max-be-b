package com.codesquad.secondhand.category.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
