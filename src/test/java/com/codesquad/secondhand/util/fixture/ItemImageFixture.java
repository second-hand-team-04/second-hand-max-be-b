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

	상품_일본_경대_이미지1(1L, 상품_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대.getId(), LocalDateTime.now(), false),
	상품_일본_경대_이미지2(1L, 상품_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대2.getId(), LocalDateTime.now(), false);

	private final Long id;
	private final Long itemId;
	private final Long ImageId;
	private final LocalDateTime createdAt;
	private final boolean isDeleted;

	ItemImageFixture(Long id, Long itemId, Long imageId, LocalDateTime createdAt, boolean isDeleted) {
		this.id = id;
		this.itemId = itemId;
		ImageId = imageId;
		this.createdAt = createdAt;
		this.isDeleted = isDeleted;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO item_image(item_id, image_id, created_at) VALUES %s",
			Arrays.stream(values())
				.map(itemImage -> String.format(
					"(%s, %s, '%s')",
					itemImage.getItemId(),
					itemImage.getImageId(),
					itemImage.getCreatedAt()))
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public boolean isDeleted() {
		return isDeleted;
	}
}
