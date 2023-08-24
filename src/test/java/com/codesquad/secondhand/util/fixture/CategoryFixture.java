package com.codesquad.secondhand.util.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CategoryFixture {

	CATEGORY1(1L, "전체보기", "https://i.ibb.co/LSkHKbL/star.png"),
	CATEGORY2(2L, "부동산", "https://i.ibb.co/41ScRXr/real-estate.png"),
	CATEGORY3(3L, "중고차", "https://i.ibb.co/bLW8sd7/car.png"),
	CATEGORY4(4L, "디지털기기", "https://i.ibb.co/cxS7Fhc/digital.png"),
	CATEGORY5(5L, "생활가전", "https://i.ibb.co/F5z7vV9/domestic.png"),
	CATEGORY6(6L, "가구/인테리어", "https://i.ibb.co/cyYH5V8/furniture.png"),
	CATEGORY7(7L, "유아동", "https://i.ibb.co/VNKYZTK/baby.png"),
	CATEGORY8(8L, "유아도서", "https://i.ibb.co/LrwjRdf/baby-book.png"),
	CATEGORY9(9L, "스포츠/레저", "https://i.ibb.co/hXVgTyd/sports.png"),
	CATEGORY10(10L, "여성잡화", "https://i.ibb.co/yPwkyg3/woman-accessories.png"),
	CATEGORY11(11L, "여성의류", "https://i.ibb.co/4fvj6SC/woman-apparel.png"),
	CATEGORY12(12L, "남성패션/잡화", "https://i.ibb.co/wwfyjyB/man-apparel.png"),
	CATEGORY13(13L, "게임/취미", "https://i.ibb.co/cwJ74M4/game.png"),
	CATEGORY14(14L, "뷰티/미용", "https://i.ibb.co/cXrrK0m/beauty.png"),
	CATEGORY15(15L, "반려동물용품", "https://i.ibb.co/CbwHdNr/pet.png"),
	CATEGORY16(16L, "도서/음반", "https://i.ibb.co/7WjKkdt/book.png"),
	CATEGORY17(17L, "티켓,교환권", "https://i.ibb.co/kBhhs2p/ticket.png"),
	CATEGORY18(18L, "생활", "https://i.ibb.co/T0mnp8m/kitchen.png"),
	CATEGORY19(19L, "가공식품", "https://i.ibb.co/S0rSyxr/processed-foods.png"),
	CATEGORY20(20L, "식물", "https://i.ibb.co/rwZhRqh/plant.png"),
	CATEGORY21(21L, "기타 중고물품", "https://i.ibb.co/tCyMPf5/etc.png"),
	CATEGORY22(22L, "삽니다", "https://i.ibb.co/g7Gc1w0/buy.png");

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
