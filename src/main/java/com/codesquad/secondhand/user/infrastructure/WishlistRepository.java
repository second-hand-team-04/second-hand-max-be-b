package com.codesquad.secondhand.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.user.domain.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

	@Modifying
	@Query("delete from Wishlist w where w.item.id = :itemId")
	void deleteByItemId(Long itemId);
}
