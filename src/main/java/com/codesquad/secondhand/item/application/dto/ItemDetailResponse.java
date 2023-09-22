package com.codesquad.secondhand.item.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.image.application.dto.ImageResponse;
import com.codesquad.secondhand.item.infrastructure.dto.ItemDetailDto;
import com.codesquad.secondhand.user.application.dto.UserItemDetailResponse;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemDetailResponse implements Serializable {

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
	private CategoryInfoResponse category;
	private UserItemDetailResponse seller;
	private List<ImageResponse> images;

	public static ItemDetailResponse of(ItemDetailDto item, int itemViewCount, WishItem wishItem, Long userId) {
		return new ItemDetailResponse(item.getId(), item.getTitle(), item.getContent(), item.getPrice(),
			0, wishItem.getNumLike(), itemViewCount, wishItem.isLiked(userId), item.getUpdatedAt(),
			item.getStatus(), item.getCategory(), item.getSeller(), ImageResponse.from(item.getImages()));
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

	public CategoryInfoResponse getCategory() {
		return category;
	}

	public UserItemDetailResponse getSeller() {
		return seller;
	}

	public List<ImageResponse> getImages() {
		return images.isEmpty() ? null : images;
	}
}
