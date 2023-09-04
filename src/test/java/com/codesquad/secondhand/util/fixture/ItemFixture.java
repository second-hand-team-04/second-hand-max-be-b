package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_가구_인테리어;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_게임_취미;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_생활;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_스포츠_레저;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_궁정동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_청운동;
import static com.codesquad.secondhand.util.fixture.StatusFixture.예약중;
import static com.codesquad.secondhand.util.fixture.StatusFixture.판매중;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.codesquad.secondhand.item.application.dto.ItemResponse;

public enum ItemFixture {

	상품_빈티지_일본_경대(1L, 유저_만두.getId(), 카테고리_가구_인테리어.getId(), 동네_서울_종로구_청운동.getId(), 판매중.getId(), "빈티지 일본 경대",
		"빈티지 일본 경대입니다", 50000, 0,
		LocalDateTime.of(2023, 7, 11, 3, 51, 0),
		LocalDateTime.of(2023, 7, 11, 3, 51, 0), false),
	상품_PS5(2L, 유저_만두.getId(), 카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId(), 예약중.getId(), "미개봉 PS5",
		"미개봉한 PS5 팝니다.", 510000, 10,
		LocalDateTime.of(2023, 7, 20, 16, 10, 0),
		LocalDateTime.of(2023, 9, 1, 12, 44, 0), false),
	상품_젤다의_전설(3L, 유저_만두.getId(), 카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId(), 판매중.getId(), "젤다의 전설 티어스 오브 킹덤",
		"젤다의 전설 티어스 오브 킹덤 팔아요", 60000, 5,
		LocalDateTime.of(2023, 8, 14, 19, 20, 0),
		LocalDateTime.of(2023, 8, 15, 8, 10, 0), false),
	상품_코렐_접시(4L, 유저_만두.getId(), 카테고리_생활.getId(), 동네_서울_종로구_궁정동.getId(), 예약중.getId(), "코렐 접시 세트",
		"한달 된 코렐 접시입니다", 80000, 20,
		LocalDateTime.of(2023, 8, 22, 11, 30, 0),
		LocalDateTime.of(2023, 8, 22, 11, 30, 0), false),
	상품_삼천리_자전거(5L, 유저_만두.getId(), 카테고리_스포츠_레저.getId(), 동네_서울_종로구_궁정동.getId(), 판매중.getId(), "삼천리 로드 자전거",
		"로드 자전거 팝니다", 100000, 42,
		LocalDateTime.of(2023, 8, 27, 15, 00, 0),
		LocalDateTime.of(2023, 8, 27, 15, 00, 0), false);

	private final Long id;
	private final Long userId;
	private final Long categoryId;
	private final Long regionId;
	private final Long statusId;
	private final String title;
	private final String content;
	private final int price;
	private final int views;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final boolean isDeleted;

	ItemFixture(Long id, Long userId, Long categoryId, Long regionId, Long statusId, String title, String content,
		int price, int views, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted) {
		this.id = id;
		this.userId = userId;
		this.categoryId = categoryId;
		this.regionId = regionId;
		this.statusId = statusId;
		this.title = title;
		this.content = content;
		this.price = price;
		this.views = views;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isDeleted = isDeleted;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `item`(user_id, category_id, region_id, status_id, title, content, price, views, created_at, updated_at) VALUES %s",
			Arrays.stream(values())
				.map(item -> String.format(
					"(%s, %s, %s, %s, '%s', '%s', %s, %s, '%s', '%s')",
					item.getUserId(),
					item.getCategoryId(),
					item.getRegionId(),
					item.getStatusId(),
					item.getTitle(),
					item.getContent(),
					item.getPrice(),
					item.getViews(),
					item.getCreatedAt(),
					item.getUpdatedAt()))
				.collect(Collectors.joining(", ")));
	}

	public ItemResponse toItemResponse() {
		return new ItemResponse(
			id, title, RegionFixture.findById(regionId).getTitle(), StatusFixture.findById(statusId).getType(),
			ImageFixture.findThumbnail(id), createdAt, updatedAt, price, 0, WishlistFixture.getWishListSize(id)
		);
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public int getPrice() {
		return price;
	}

	public int getViews() {
		return views;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public boolean isDeleted() {
		return isDeleted;
	}
}
