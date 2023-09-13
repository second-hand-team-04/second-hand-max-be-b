package com.codesquad.secondhand.user.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.item.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyWishlistResponse {

	private Long id;

	private String title;

	private String region;

	private String status;

	private String thumbnailUrl;

	private LocalDateTime updatedAt;

	private Integer price;

	private int numChat;

	private int numLikes;

	private Long sellerId;

	public static MyWishlistResponse from(Item item) {
		return new MyWishlistResponse(
			item.getId(),
			item.getTitle(),
			item.getRegion().getTitle(),
			item.getStatus().getType(),
			item.getThumbnailUrl(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getChatCount(),
			item.getWishlistCount(),
			item.getSellerId()
		);
	}

	public static List<MyWishlistResponse> from(List<Item> items) {
		return items.stream()
			.map(MyWishlistResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
