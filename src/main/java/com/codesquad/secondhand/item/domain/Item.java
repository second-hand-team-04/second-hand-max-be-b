package com.codesquad.secondhand.item.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
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

	private Integer price;

	private int views;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private boolean isDeleted;

	@Embedded
	private Images images = new Images();

	@OneToMany(mappedBy = "item")
	private List<Wishlist> wishlists = new ArrayList<>();

	public Item(String title, Integer price, String content, List<Image> images, Category category, Region region,
		Status status, User user) {
		this.title = title;
		this.price = price;
		this.content = content;
		this.category = category;
		this.region = region;
		this.status = status;
		this.user = user;
		addImage(images);
	}

	public void addImage(List<Image> images) {
		if (Objects.nonNull(images) && !images.isEmpty()) {
			List<ItemImage> itemImages = images.stream()
				.map(image -> ItemImage.of(this, image))
				.collect(Collectors.toUnmodifiableList());
			this.images.addImage(itemImages);
		}
	}

	public void addImage(Image image) {
		if (Objects.nonNull(image)) {
			this.images.addImage(ItemImage.of(this, image));
		}
	}

	public void increaseViewCount() {
		views++;
	}

	public int getWishlistCount() {
		return wishlists.size();
	}

	public boolean isMyWishlisted(Long accountUserId) {
		return wishlists.stream()
			.anyMatch(w -> w.getUser().equalsId(accountUserId));
	}

	public int getChatCount() {
		return 0;
	}

	public String getThumbnailUrl() {
		return images.getThumbnailUrl();
	}

	public List<Image> getImages() {
		return images.getImages();
	}
}
