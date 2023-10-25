package com.codesquad.secondhand.user.infrastructure;

import java.util.Optional;

import com.codesquad.secondhand.user.infrastructure.dto.WishItem;

public interface WishlistCustomRepository {

	Optional<WishItem> findRedisWishItemByItemId(Long itemId);

	WishItem findWishItemByItemId(Long itemId);
}
