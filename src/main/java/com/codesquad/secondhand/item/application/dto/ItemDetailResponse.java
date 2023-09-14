package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.codesquad.secondhand.image.application.dto.ImageResponse;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.category.application.dto.CategoryItemDetailResponse;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.user.application.dto.UserItemDetailResponse;
import com.codesquad.secondhand.user.domain.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemDetailResponse {

	private Long id;
	private String title;
	private String content;
	private Integer price;
	private int numChat;
	private int numLikes;
	private int numViews;
	private boolean isLiked;
	private LocalDateTime updatedAt;
	private StatusItemDetailResponse status;
	private CategoryItemDetailResponse category;
	private UserItemDetailResponse seller;
	private List<ImageResponse> images;

	public static ItemDetailResponse from(Item item, User accountUser) {
		Status status = item.getStatus();
		Category category = item.getCategory();
		User user = item.getUser();
		return new ItemDetailResponse(item.getId(), item.getTitle(), item.getContent(), item.getPrice(),
			item.getChatCount(), item.getWishlistCount(), item.getViews(), item.isMyWishlisted(accountUser), item.getUpdatedAt(),
			StatusItemDetailResponse.from(status), CategoryItemDetailResponse.from(category),
			UserItemDetailResponse.from(user), ImageResponse.from(item.getImages()));
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Integer getPrice() {
		return price;
	}

	public int getNumChat() {
		return numChat;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public int getNumViews() {
		return numViews;
	}

	public boolean getIsLiked() {
		return isLiked;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getStatus() {
		return status.getStatus();
	}

	public String getCategory() {
		return category.getCategory();
	}

	public UserItemDetailResponse getSeller() {
		return seller;
	}

	public List<ImageResponse> getImages() {
		return images.isEmpty() ? null : images;
	}
}
