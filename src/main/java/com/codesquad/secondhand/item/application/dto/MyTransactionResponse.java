package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.item.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTransactionResponse {
	private Long id;
	private String title;
	private String region;
	private LocalDateTime updatedAt;
	private Integer price;
	private String thumbnail;
	private Long sellerId;

	public static MyTransactionResponse of(Item item) {
		return new MyTransactionResponse(
			item.getId(),
			item.getTitle(),
			item.getRegion().getTitle(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getThumbnailUrl(),
			item.getSellerId()
		);
	}

	public static List<MyTransactionResponse> of(List<Item> items) {
		return items.stream()
			.map(MyTransactionResponse::of)
			.collect(Collectors.toUnmodifiableList());
	}
}
