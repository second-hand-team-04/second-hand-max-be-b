package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemDao {

	@Query("SELECT i FROM Item i JOIN FETCH i.user u LEFT JOIN FETCH i.category c JOIN FETCH i.region r JOIN FETCH i.status s LEFT JOIN FETCH i.images ii LEFT JOIN FETCH ii.image WHERE i.isDeleted = false ORDER BY i.createdAt DESC")
	Slice<Item> findSliceByCategoryIdAndRegionId(Pageable pageable, Long categoryId, Long regionId);
}
