package com.codesquad.secondhand.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.common.exception.item.ItemNotFoundException;
import com.codesquad.secondhand.common.exception.wishlist.WishlistDuplicationException;
import com.codesquad.secondhand.common.exception.wishlist.WishlistNotIncludeException;
import com.codesquad.secondhand.item.domain.Item;

@Embeddable
public class MyWishlists {

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Wishlist> wishlists = new ArrayList<>();

	public void addWishList(Wishlist wishlist) {
		validateNotDeleted(wishlist.getItem());
		validateDuplication(wishlist);
		wishlists.add(wishlist);
	}

	public void removeItem(Item item) {
		Wishlist wishlist = findByItem(item)
			.orElseThrow(WishlistNotIncludeException::new);
		wishlists.remove(wishlist);
	}

	private Optional<Wishlist> findByItem(Item item) {
		return wishlists.stream()
			.filter(i -> i.equalsItem(item.getId()))
			.findAny();
	}

	private void validateDuplication(Wishlist wishlist) {
		if (wishlists.stream().anyMatch(w -> w.equalsItem(wishlist.getItem()))) {
			throw new WishlistDuplicationException();
		}
	}

	private void validateNotDeleted(Item item) {
		if (item.isDeleted()) {
			throw new ItemNotFoundException();
		}
	}
}
