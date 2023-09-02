package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.item.domain.Item;

import lombok.Getter;

@Getter
public class ItemResponse {

	private Long id;

	private String title;

	private String region;

	private String status;

	private String thumbnail;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Integer price;

	private Integer numChat;

	private Integer numLikes;

	public ItemResponse(Long id, String title, String region, String status, String thumbnail, LocalDateTime createdAt,
		LocalDateTime updatedAt, Integer price, Integer numChat, Integer numLikes) {
		this.id = id;
		this.title = title;
		this.region = region;
		this.status = status;
		this.thumbnail = thumbnail;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.price = price;
		this.numChat = numChat;
		this.numLikes = numLikes;
	}

	public static ItemResponse of(Item item, Image defaultImage) {
		return new ItemResponse(
			item.getId(),
			item.getTitle(),
			item.getRegion().getTitle(),
			item.getStatus().getType(),
			item.getThumbnail(defaultImage).getImageUrl(),
			item.getCreatedAt(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getChatCount(),
			item.getWishlistCount()
		);
	}

	public static List<ItemResponse> of(List<Item> items, Image defaultImage) {
		return items.stream()
			.map(item -> ItemResponse.of(item, defaultImage))
			.collect(Collectors.toUnmodifiableList());
	}
}
