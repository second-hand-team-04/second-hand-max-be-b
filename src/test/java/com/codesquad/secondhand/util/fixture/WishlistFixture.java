package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_PS5;
import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_젤다의_전설;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_보노;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public enum WishlistFixture {

	관심목록_유저_만두_상품_PS5(1L, 유저_만두.getId(), 상품_PS5.getId()),
	관심목록_유저_만두_상품_젤다의_전설(2L, 유저_만두.getId(), 상품_젤다의_전설.getId());

	private final Long id;
	private final Long userId;
	private final Long itemId;

	WishlistFixture(Long id, Long userId, Long itemId) {
		this.id = id;
		this.userId = userId;
		this.itemId = itemId;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO wishlist(user_id, item_id) VALUES %s",
			Arrays.stream(values())
				.map(wishlist -> String.format(
					"(%s, %s)",
					wishlist.getUserId(),
					wishlist.getItemId()))
				.collect(Collectors.joining(", ")));
	}

	public static int getWishListSize(Long itemId) {
		return (int)Arrays.stream(values())
			.filter(w -> Objects.equals(w.itemId, itemId))
			.count();
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getItemId() {
		return itemId;
	}
}
