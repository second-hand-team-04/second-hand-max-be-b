package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

	public static MyTransactionResponse from(ItemResponse itemResponse) {
		return new MyTransactionResponse(
			itemResponse.getId(),
			itemResponse.getTitle(),
			itemResponse.getRegion(),
			itemResponse.getStatus(),
			itemResponse.getUpdatedAt(),
			itemResponse.getPrice(),
			itemResponse.getThumbnailUrl(),
			itemResponse.getSellerId(),
			itemResponse.getNumChat(),
			itemResponse.getNumLikes()
		);
	}

	public static List<MyTransactionResponse> from(List<ItemResponse> itemResponses) {
		return itemResponses.stream()
			.map(MyTransactionResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
