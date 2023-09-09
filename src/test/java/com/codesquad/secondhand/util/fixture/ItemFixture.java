package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_가구_인테리어;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_게임_취미;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_생활;
import static com.codesquad.secondhand.util.fixture.CategoryFixture.카테고리_스포츠_레저;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_궁정동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_청운동;
import static com.codesquad.secondhand.util.fixture.StatusFixture.판매중;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;

import java.time.LocalDateTime;
import java.util.List;

import com.codesquad.secondhand.Image.application.dto.ImageResponse;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.ItemResponse;

public enum ItemFixture {

	상품_빈티지_일본_경대(1L, 유저_만두.getId(), 카테고리_가구_인테리어.getId(), 동네_서울_종로구_청운동.getId(), 판매중.getId(),
		"빈티지 일본 경대", "빈티지 일본 경대입니다", 50000, false),
	상품_PS5(2L, 유저_만두.getId(), 카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId(), 판매중.getId(),
		"미개봉 PS5", "미개봉한 PS5 팝니다.", 510000, false),
	상품_젤다의_전설(3L, 유저_만두.getId(), 카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId(), 판매중.getId(),
		"젤다의 전설 티어스 오브 킹덤", "젤다의 전설 티어스 오브 킹덤 팔아요", 60000, false),
	상품_코렐_접시(4L, 유저_만두.getId(), 카테고리_생활.getId(), 동네_서울_종로구_궁정동.getId(), 판매중.getId(),
		"코렐 접시 세트", "한달 된 코렐 접시입니다", 80000, false),
	상품_삼천리_자전거(5L, 유저_만두.getId(), 카테고리_스포츠_레저.getId(), 동네_서울_종로구_궁정동.getId(), 판매중.getId(),
		"삼천리 로드 자전거", "로드 자전거 팝니다", 100000, false);

	private final Long id;
	private final Long userId;
	private final Long categoryId;
	private final Long regionId;
	private final Long statusId;
	private final String title;
	private final String content;
	private final int price;
	private final boolean isDeleted;

	ItemFixture(Long id, Long userId, Long categoryId, Long regionId, Long statusId, String title, String content,
		int price, boolean isDeleted) {
		this.id = id;
		this.userId = userId;
		this.categoryId = categoryId;
		this.regionId = regionId;
		this.statusId = statusId;
		this.title = title;
		this.content = content;
		this.price = price;
		this.isDeleted = isDeleted;
	}

	public ItemResponse toItemResponse(String thumbnail, int numChat, int numLikes) {
		return new ItemResponse(
			id, title, RegionFixture.findById(regionId).getTitle(), StatusFixture.findById(statusId).getType(),
			thumbnail, null, null, price, numChat, numLikes
		);
	}

	public ItemDetailResponse toItemDetailResponse(int views, int numChat, int numLikes, List<ImageResponse> imageResponses) {
		return new ItemDetailResponse(
			id, title, content, price, numChat,
			numLikes, views, false, LocalDateTime.now(),
			StatusFixture.findById(statusId).toStatusItemDetailResponse(),
			CategoryFixture.findById(categoryId).toCategoryItemDetailResponse(),
			UserFixture.findById(userId).toUserItemDetailResponse(), imageResponses);
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

	public boolean isDeleted() {
		return isDeleted;
	}
}
