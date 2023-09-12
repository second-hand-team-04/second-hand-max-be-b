package com.codesquad.secondhand.item.infrastructure;

import static com.codesquad.secondhand.image.domain.QImage.*;
import static com.codesquad.secondhand.item.domain.QItem.*;
import static com.codesquad.secondhand.item.domain.QItemImage.*;
import static com.codesquad.secondhand.item.domain.QStatus.*;
import static com.codesquad.secondhand.region.domain.QRegion.*;
import static com.codesquad.secondhand.user.domain.QWishlist.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.item.domain.Item;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<Item> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable) {
		List<Item> content = jpaQueryFactory.selectFrom(item)
			.innerJoin(item.region, region).fetchJoin()
			.innerJoin(item.status, status).fetchJoin()
			.leftJoin(item.images.itemImages, itemImage).fetchJoin()
			.leftJoin(itemImage.image, image).fetchJoin()
			.where(
				categoryIdEq(categoryId),
				item.region.id.eq(regionId),
				item.isDeleted.isFalse()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.orderBy(item.updatedAt.desc(), itemImage.id.asc())
			.fetch();

		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	@Override
	public Slice<Item> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable) {
		List<Item> content = jpaQueryFactory.selectFrom(item)
			.innerJoin(item.wishlists, wishlist)
			.innerJoin(item.status, status).fetchJoin()
			.leftJoin(item.images.itemImages, itemImage).fetchJoin()
			.leftJoin(itemImage.image, image).fetchJoin()
			.where(
				wishlist.user.id.eq(userId),
				categoryIdEq(categoryId),
				item.isDeleted.isFalse()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.orderBy(item.updatedAt.desc(), itemImage.id.asc())
			.fetch();

		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	private BooleanExpression categoryIdEq(Long categoryId) {
		if (categoryId == null || categoryId == 1L) {
			return null;
		}

		return item.category.id.eq(categoryId);
	}

	private boolean getHasNext(int pageSize, List<Item> items) {
		if (items.size() > pageSize) {
			items.remove(pageSize);
			return true;
		}

		return false;
	}

	@Override
	public Slice<Item> findAllMyTransactionByStatus(Long userId, List<Long> statusIds, Pageable pageable) {
		List<Item> content = jpaQueryFactory.selectFrom(item)
			.innerJoin(item.region, region).fetchJoin()
			.leftJoin(item.images.itemImages, itemImage).fetchJoin()
			.leftJoin(itemImage.image, image).fetchJoin()
			.where(
				item.user.id.eq(userId),
				item.isDeleted.isFalse(),
				statusIdIn(statusIds)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.orderBy(item.updatedAt.desc(), itemImage.id.asc())
			.fetch();
		return new SliceImpl<>(content, pageable, getHasNext(pageable.getPageSize(), content));
	}

	private BooleanExpression statusIdIn(List<Long> statusIds) {
		if (Objects.isNull(statusIds) || statusIds.isEmpty()) {
			return null;
		}

		return item.status.id.in(statusIds);
	}
}
