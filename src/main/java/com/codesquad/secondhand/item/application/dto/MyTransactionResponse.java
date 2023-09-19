package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.item.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTransactionResponse {

	private Long id;
	private String title;
	private String region;
	private String status;
	private LocalDateTime updatedAt;
	private Integer price;
	private String thumbnailUrl;
	private Long sellerId;
	private int numChat;
	private int numLikes;
	private CategoryInfoResponse category;

	public static MyTransactionResponse of(Item item) {
		return new MyTransactionResponse(
			item.getId(),
			item.getTitle(),
			item.getRegion().getTitle(),
			item.getStatus().getType(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getThumbnailUrl(),
			item.getSellerId(),
			item.getChatCount(),
			item.getWishlistCount(),
			CategoryInfoResponse.from(item.getCategory())
		);
	}

	public static List<MyTransactionResponse> of(List<Item> items) {
		return items.stream()
			.map(MyTransactionResponse::of)
			.collect(Collectors.toUnmodifiableList());
	}
}
