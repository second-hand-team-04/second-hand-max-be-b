package com.codesquad.secondhand.util.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.codesquad.secondhand.category.application.dto.CategoryItemDetailResponse;

public enum CategoryFixture {

	카테고리_전체보기(1L, "전체보기", "https://i.ibb.co/LSkHKbL/star.png"),
	카테고리_부동산(2L, "부동산", "https://i.ibb.co/41ScRXr/real-estate.png"),
	카테고리_중고차(3L, "중고차", "https://i.ibb.co/bLW8sd7/car.png"),
	카테고리_디지털기기(4L, "디지털기기", "https://i.ibb.co/cxS7Fhc/digital.png"),
	카테고리_생활가전(5L, "생활가전", "https://i.ibb.co/F5z7vV9/domestic.png"),
	카테고리_가구_인테리어(6L, "가구/인테리어", "https://i.ibb.co/cyYH5V8/furniture.png"),
	카테고리_유아동(7L, "유아동", "https://i.ibb.co/VNKYZTK/baby.png"),
	카테고리_유아도서(8L, "유아도서", "https://i.ibb.co/LrwjRdf/baby-book.png"),
	카테고리_스포츠_레저(9L, "스포츠/레저", "https://i.ibb.co/hXVgTyd/sports.png"),
	카테고리_여성잡화(10L, "여성잡화", "https://i.ibb.co/yPwkyg3/woman-accessories.png"),
	카테고리_여성의류(11L, "여성의류", "https://i.ibb.co/4fvj6SC/woman-apparel.png"),
	카테고리_남성패션_잡화(12L, "남성패션/잡화", "https://i.ibb.co/wwfyjyB/man-apparel.png"),
	카테고리_게임_취미(13L, "게임/취미", "https://i.ibb.co/cwJ74M4/game.png"),
	카테고리_뷰티_미용(14L, "뷰티/미용", "https://i.ibb.co/cXrrK0m/beauty.png"),
	카테고리_반려동물용품(15L, "반려동물용품", "https://i.ibb.co/CbwHdNr/pet.png"),
	카테고리_도서_음반(16L, "도서/음반", "https://i.ibb.co/7WjKkdt/book.png"),
	카테고리_티켓_교환권(17L, "티켓,교환권", "https://i.ibb.co/kBhhs2p/ticket.png"),
	카테고리_생활(18L, "생활", "https://i.ibb.co/T0mnp8m/kitchen.png"),
	카테고리_가공식품(19L, "가공식품", "https://i.ibb.co/S0rSyxr/processed-foods.png"),
	카테고리_식물(20L, "식물", "https://i.ibb.co/rwZhRqh/plant.png"),
	카테고리_기타_중고물품(21L, "기타 중고물품", "https://i.ibb.co/tCyMPf5/etc.png"),
	카테고리_삽니다(22L, "삽니다", "https://i.ibb.co/g7Gc1w0/buy.png");

	private final Long id;
	private final String title;
	private final String imageUrl;

	CategoryFixture(Long id, String title, String imageUrl) {
		this.id = id;
		this.title = title;
		this.imageUrl = imageUrl;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO category(title, image_url) VALUES %s",
			Arrays.stream(values())
				.map(c -> String.format(
					"('%s', '%s')",
					c.getTitle(),
					c.getImageUrl()))
				.collect(Collectors.joining(", ")));
	}

	public static CategoryFixture findById(Long id) {
		return Arrays.stream(values())
			.filter(c -> Objects.equals((c.getId()), id))
			.findAny()
			.orElseThrow();
	}

	public static List<String> getTitles() {
		return Arrays.stream(values())
			.map(CategoryFixture::getTitle)
			.collect(Collectors.toUnmodifiableList());
	}

	public static List<String> getImageUrls() {
		return Arrays.stream(values())
			.map(CategoryFixture::getImageUrl)
			.collect(Collectors.toUnmodifiableList());
	}

	public static CategoryItemDetailResponse findCategoryItemDetailResponseById(Long id) {
		return findById(id).toCategoryItemDetailResponse();
	}

	public CategoryItemDetailResponse toCategoryItemDetailResponse() {
		return new CategoryItemDetailResponse(title);
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
