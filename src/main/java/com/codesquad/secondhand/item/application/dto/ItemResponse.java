package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;

import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ItemResponse {

	private Long id;

	private String title;

	private String region;

	private String status;

	private String thumbnailUrl;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Integer price;

	private Integer numChat;

	private Integer numLikes;

	private Long sellerId;

	private Category category;

	public ItemResponse(Long id, String title, String region, String status, String thumbnailUrl,
		LocalDateTime createdAt, LocalDateTime updatedAt, Integer price, Integer numChat, Integer numLikes,
		Long sellerId, Category category) {
		this.id = id;
		this.title = title;
		this.region = region;
		this.status = status;
		this.thumbnailUrl = thumbnailUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.price = price;
		this.numChat = numChat;
		this.numLikes = numLikes;
		this.sellerId = sellerId;
		this.category = category;
	}
}
