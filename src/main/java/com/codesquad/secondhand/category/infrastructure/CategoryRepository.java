package com.codesquad.secondhand.category.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("select distinct c from Category c left outer join Item i on c.id = i.category.id left outer join Wishlist w on i.id = w.item.id where w.user.id = :userId or c.id = 1")
	List<Category> findCategoriesOnMyWishlistByUserId(Long userId);
}
