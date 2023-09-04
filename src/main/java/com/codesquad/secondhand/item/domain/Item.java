package com.codesquad.secondhand.item.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.domain.Wishlist;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	private Region region;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	private String title;

	private String content;

	private int price;

	private int views;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private boolean isDeleted;

	@OneToMany(mappedBy = "item")
	private List<ItemImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "item")
	private List<Wishlist> wishlists = new ArrayList<>();

	public int getWishlistCount() {
		return wishlists.size();
	}

	public int getChatCount() {
		return 0;
	}

	public Image getThumbnail(Image defaultImage) {
		return images.stream()
			.findFirst()
			.map(ItemImage::getImage)
			.orElse(defaultImage);
	}
}
