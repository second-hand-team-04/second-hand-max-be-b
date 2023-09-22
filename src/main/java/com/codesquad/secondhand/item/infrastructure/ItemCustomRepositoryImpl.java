package com.codesquad.secondhand.item.infrastructure;

import static com.codesquad.secondhand.category.domain.QCategory.category;
import static com.codesquad.secondhand.common.util.RedisUtil.ITEM_VIEW_COUNT;
import static com.codesquad.secondhand.image.domain.QImage.image;
import static com.codesquad.secondhand.item.domain.QItem.item;
import static com.codesquad.secondhand.item.domain.QItemImage.itemImage;
import static com.codesquad.secondhand.item.domain.QStatus.status;
import static com.codesquad.secondhand.region.domain.QRegion.region;
import static com.codesquad.secondhand.user.domain.QUser.user;
import static com.codesquad.secondhand.user.domain.QWishlist.wishlist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.jpa.JPAExpressions.select;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.common.util.RedisUtil;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.StatusItemDetailResponse;
import com.codesquad.secondhand.item.infrastructure.dto.ItemDetailDto;
import com.codesquad.secondhand.user.application.dto.UserItemDetailResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final RedisUtil redisUtil;

	@Override
	public Slice<ItemResponse> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable) {
		List<ItemResponse> content = getItemResponses(pageable, categoryIdEq(categoryId),
			item.region.id.eq(regionId), item.isDeleted.isFalse());
		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	private List<ItemResponse> getItemResponses(Pageable pageable, Predicate... predicates) {
		return jpaQueryFactory.select(
				Projections.fields(
					ItemResponse.class,
					item.id,
					item.title,
					region.title.as("region"),
					status.type.as("status"),
					getThumbnailUrl(),
					item.createdAt,
					item.updatedAt,
					item.price,
					item.views.multiply(0).as("numChat"),
					wishlist.countDistinct().castToNum(Integer.class).as("numLikes"),
					user.id.as("sellerId"),
					item.category))
			.from(item)
			.innerJoin(item.region, region)
			.innerJoin(item.status, status)
			.innerJoin(item.user, user)
			.leftJoin(wishlist).on(wishlist.item.id.eq(item.id))
			.where(predicates)
			.groupBy(item.id, item.title, region.title, status.type,
				item.createdAt, item.updatedAt, item.price, item.views, user.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.orderBy(item.updatedAt.desc())
			.fetch();
	}

	private Expression<String> getThumbnailUrl() {
		return ExpressionUtils.as(
			select(image.imageUrl)
				.from(item.images.itemImages, itemImage)
				.innerJoin(itemImage.image, image)
				.where(itemImage.id.eq(
					select(itemImage.id.min())
						.from(item.images.itemImages, itemImage)
						.where(itemImage.item.id.eq(item.id)))), "thumbnailUrl");
	}

	@Override
	public Slice<ItemResponse> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable) {
		List<ItemResponse> content = getItemResponses(pageable, categoryIdEq(categoryId),
			wishlist.user.id.eq(userId), item.isDeleted.isFalse());
		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	@Override
	public Optional<ItemDetailDto> findDetailById(Long id) {
		Map<Long, ItemDetailDto> transform = jpaQueryFactory
			.from(item)
			.innerJoin(item.category, category)
			.innerJoin(item.status, status)
			.innerJoin(item.user, user)
			.leftJoin(item.images.itemImages, itemImage)
			.leftJoin(itemImage.image, image)
			.where(item.id.eq(id), item.isDeleted.isFalse())
			.transform(
				groupBy(item.id).as(
					Projections.fields(
						ItemDetailDto.class,
						item.id,
						item.title,
						item.content,
						item.price,
						item.updatedAt,
						Projections.fields(StatusItemDetailResponse.class, status.type.as("status")).as("status"),
						Projections.fields(CategoryInfoResponse.class, category.id, category.title).as("category"),
						Projections.fields(UserItemDetailResponse.class, user.id, user.nickname).as("seller"),
						list(image).as("images")
					)
				)
			);

		return Optional.ofNullable(transform.get(id));
	}

	private BooleanExpression categoryIdEq(Long categoryId) {
		if (categoryId == null || categoryId == 1L) {
			return null;
		}

		return item.category.id.eq(categoryId);
	}

	private <T> boolean getHasNext(int pageSize, List<T> items) {
		if (items.size() > pageSize) {
			items.remove(pageSize);
			return true;
		}

		return false;
	}

	@Override
	public Slice<ItemResponse> findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable) {
		List<ItemResponse> content = getItemResponses(pageable, item.user.id.eq(userId),
			item.isDeleted.isFalse(), statusIdIn(statusIds));
		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	private BooleanExpression statusIdIn(List<Long> statusIds) {
		if (Objects.isNull(statusIds) || statusIds.isEmpty()) {
			return null;
		}

		return item.status.id.in(statusIds);
	}

	@Override
	public int incrementViewCount(Long id) {
		String redisViewCount = redisUtil.getForString(ITEM_VIEW_COUNT, id);

		if (Objects.isNull(redisViewCount)) {
			int viewCount = findViewCount(id) + 1;
			redisUtil.putForString(ITEM_VIEW_COUNT, id, String.valueOf(viewCount));
			return viewCount;
		}

		return redisUtil.incrementForValue(ITEM_VIEW_COUNT, id, 1L)
			.intValue();
	}

	private int findViewCount(Long id) {
		Integer viewCount = jpaQueryFactory.select(item.views)
			.from(item)
			.where(item.id.eq(id))
			.fetchOne();
		return Objects.isNull(viewCount) ? 0 : viewCount;
	}

	@Override
	public Map<Long, Integer> findAllRedisItemViewCount() {
		return redisUtil.getKeyValueMapForObjectAndCacheManager(ITEM_VIEW_COUNT, Integer.class);
	}
}
