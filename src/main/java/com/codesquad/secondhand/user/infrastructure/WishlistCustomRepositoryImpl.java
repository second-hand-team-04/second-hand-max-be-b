package com.codesquad.secondhand.user.infrastructure;

import static com.codesquad.secondhand.common.util.RedisUtil.WISH_ITEM;
import static com.codesquad.secondhand.item.domain.QItem.item;
import static com.codesquad.secondhand.user.domain.QWishlist.wishlist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.common.util.RedisUtil;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WishlistCustomRepositoryImpl implements WishlistCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final RedisUtil redisUtil;

	@Override
	public Optional<WishItem> findRedisWishItemByItemId(Long itemId) {
		WishItem wishItem = redisUtil.getCacheObject(WISH_ITEM, itemId, WishItem.class);
		return Optional.ofNullable(wishItem);
	}

	@Override
	public WishItem findWishItemByItemId(Long itemId) {
		WishItem wishItem = jpaQueryFactory.from(wishlist)
			.where(wishlist.item.id.eq(itemId))
			.transform(
				groupBy(item.id).as(
					Projections.fields(
						WishItem.class,
						list(wishlist.user.id).as("wishUserId")
					)
				)
			).get(itemId);

		return WishItem.createNonNullInstance(wishItem);
	}
}
