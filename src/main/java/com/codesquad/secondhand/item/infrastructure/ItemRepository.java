package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

	@Query("select i.views from Item i where i.id = :id")
	int findViewsById(Long id);

	@Modifying
	@Query("update Item i set i.views = :views where i.id = :id")
	void updateViewsByIdAndViews(Long id, int views);
}
