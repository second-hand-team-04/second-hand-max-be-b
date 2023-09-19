package com.codesquad.secondhand.user.infrastructure;

import java.util.Optional;

import com.codesquad.secondhand.user.infrastructure.dto.WishItem;

public interface WishlistCustomRepository {

	Optional<WishItem> findRedisWishItemByItemId(Long id);

	void incrementNumLikes(Long itemId, boolean isLiked);

	void decrementNumLikes(Long itemId, boolean isLiked);

	void createRedisWishItem(Long itemId, WishItem wishItem);
}
