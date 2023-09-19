package com.codesquad.secondhand.user.infrastructure;

import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM;
import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM_EXPIRE;
import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM_IS_LIKED;
import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM_NUM_LIKES;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.common.util.RedisUtil;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WishlistCustomRepositoryImpl implements WishlistCustomRepository {

	private final RedisUtil redisUtil;

	@Override
	public Optional<WishItem> findRedisWishItemByItemId(Long id) {
		Integer numLikes = redisUtil.getForHash(WISH_ITEM, id, WISH_ITEM_NUM_LIKES, Integer.class);
		Boolean isLiked = redisUtil.getForHash(WISH_ITEM, id, WISH_ITEM_IS_LIKED, Boolean.class);

		if (Objects.isNull(numLikes) || Objects.isNull(isLiked)) {
			return Optional.empty();
		}

		return Optional.of(new WishItem(numLikes, isLiked));
	}

	@Override
	public void incrementNumLikes(Long itemId, boolean isLiked) {
		redisUtil.incrementForHash(WISH_ITEM, itemId, WISH_ITEM_NUM_LIKES, 1L);
		redisUtil.putForHash(WISH_ITEM, itemId, WISH_ITEM_IS_LIKED, isLiked);
	}

	@Override
	public void decrementNumLikes(Long itemId, boolean isLiked) {
		redisUtil.incrementForHash(WISH_ITEM, itemId, WISH_ITEM_NUM_LIKES, -1L);
		redisUtil.putForHash(WISH_ITEM, itemId, WISH_ITEM_IS_LIKED, isLiked);
	}

	@Override
	public void createRedisWishItem(Long itemId, WishItem wishItem) {
		redisUtil.putForHash(WISH_ITEM, itemId, WISH_ITEM_NUM_LIKES, wishItem.getNumLike());
		redisUtil.putForHash(WISH_ITEM, itemId, WISH_ITEM_IS_LIKED, wishItem.isLiked());
		redisUtil.expiredForObject(WISH_ITEM, itemId, WISH_ITEM_EXPIRE);
	}
}
