package com.codesquad.secondhand.item.infrastructure;

import static com.codesquad.secondhand.Image.domain.QImage.image;
import static com.codesquad.secondhand.item.domain.QItem.item;
import static com.codesquad.secondhand.item.domain.QItemImage.itemImage;
import static com.codesquad.secondhand.item.domain.QStatus.status;
import static com.codesquad.secondhand.region.domain.QRegion.region;

import java.util.List;

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
public class ItemDaoImpl implements ItemDao {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<Item> findByCategoryIdAndRegionId(Long categoryId, Long regionId, Pageable pageable) {
		List<Item> content = jpaQueryFactory.selectFrom(item)
			.innerJoin(item.region, region).fetchJoin()
			.innerJoin(item.status, status).fetchJoin()
			.leftJoin(item.images, itemImage).fetchJoin()
			.leftJoin(itemImage.image, image).fetchJoin()
			.where(
				categoryIdEq(categoryId),
				item.region.id.eq(regionId),
				item.isDeleted.eq(false)
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
}
