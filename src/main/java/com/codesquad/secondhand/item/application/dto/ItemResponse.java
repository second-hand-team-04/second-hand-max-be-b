package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.region.domain.Region;

import lombok.Getter;

@Getter
public class ItemResponse {

	private Long id;

	private String title;

	private Region region;

	private String status;

	private LocalDateTime postedAt;

	private LocalDateTime updatedAt;

	private Integer price;

	private Integer numChat;

	private Integer numLikes;

	private ItemResponse(Long id, String title, Region region, String status, LocalDateTime postedAt,
		LocalDateTime updatedAt, Integer price, Integer numChat, Integer numLikes) {
		this.id = id;
		this.title = title;
		this.region = region;
		this.status = status;
		this.postedAt = postedAt;
		this.updatedAt = updatedAt;
		this.price = price;
		this.numChat = numChat;
		this.numLikes = numLikes;
	}

	public static ItemResponse from(Item item) {
		return new ItemResponse(
			item.getId(),
			item.getTitle(),
			item.getRegion(),
			item.getStatus(),
			item.getPostedAt(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getChatCount(),
			item.getLikeCount()
		);
	}

	public static List<ItemResponse> from(List<Item> items) {
		return items.stream()
			.map(item -> ItemResponse.from(item))
			.collect(Collectors.toUnmodifiableList());
	}
}
