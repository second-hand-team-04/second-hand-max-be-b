package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.ImageFixture.이미지_빈티지_일본_경대;
import static com.codesquad.secondhand.util.fixture.ImageFixture.이미지_빈티지_일본_경대2;
import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_빈티지_일본_경대;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum ItemImageFixture {

	상품_일본_경대_이미지1(1L, 상품_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대.getId()),
	상품_일본_경대_이미지2(2L, 상품_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대2.getId());

	private final Long id;
	private final Long itemId;
	private final Long ImageId;

	ItemImageFixture(Long id, Long itemId, Long imageId) {
		this.id = id;
		this.itemId = itemId;
		ImageId = imageId;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO item_image(item_id, image_id) VALUES %s",
			Arrays.stream(values())
				.map(itemImage -> String.format(
					"(%s, %s)",
					itemImage.getItemId(),
					itemImage.getImageId()))
				.collect(Collectors.joining(", ")));
	}

	public static List<ItemImageFixture> findAllByItemId(Long id) {
		return Arrays.stream(values())
			.filter(itemImage -> Objects.equals((itemImage.getItemId()), id))
			.collect(Collectors.toUnmodifiableList());
	}

	public Long getId() {
		return id;
	}

	public Long getItemId() {
		return itemId;
	}

	public Long getImageId() {
		return ImageId;
	}
}
