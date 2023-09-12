package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.item.domain.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

	@Modifying
	@Query("delete from ItemImage ii where ii.item.id = :itemId")
	void deleteAllByItemId(Long itemId);
}
