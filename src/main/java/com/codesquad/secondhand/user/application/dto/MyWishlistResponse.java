package com.codesquad.secondhand.user.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.item.application.dto.ItemResponse;

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

	private CategoryInfoResponse category;

	public static MyWishlistResponse from(ItemResponse itemResponse) {
		return new MyWishlistResponse(
			itemResponse.getId(),
			itemResponse.getTitle(),
			itemResponse.getRegion(),
			itemResponse.getStatus(),
			itemResponse.getThumbnailUrl(),
			itemResponse.getUpdatedAt(),
			itemResponse.getPrice(),
			itemResponse.getNumChat(),
			itemResponse.getNumLikes(),
			itemResponse.getSellerId(),
			CategoryInfoResponse.from(itemResponse.getCategory())
		);
	}

	public static List<MyWishlistResponse> from(List<ItemResponse> itemResponses) {
		return itemResponses.stream()
			.map(MyWishlistResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
