package com.codesquad.secondhand.item.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.common.exception.item.ItemImageEmptyException;
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

	private Integer price;

	private int views;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	private boolean isDeleted;

	@Embedded
	private Images images = new Images();

	@OneToMany(mappedBy = "item")
	private List<Wishlist> wishlists = new ArrayList<>();

	public Item(String title, Integer price, String content, List<Image> images, Category category, Region region) {
		this.title = title;
		this.price = price;
		this.content = content;
		//FIXME 리팩토링 필요
		this.images.addItemImage(
			images.stream()
				.map(image -> ItemImage.of(this, image))
				.collect(Collectors.toUnmodifiableList())
		);
		this.category = category;
		this.region = region;
	}

	public void addImage(List<Image> images) {
		if (Objects.nonNull(images)) {
			this.images.addItemImage(
				images.stream()
					.map(image -> ItemImage.of(this, image))
					.collect(Collectors.toList())
			);
		}
	}

	public void addImage(Image image) {
		if (Objects.nonNull(image)) {
			this.images.addItemImage(ItemImage.of(this, image));
		}
	}

	public void updateStatus(Status status) {
		if(Objects.nonNull(status) && StatusType.existsById(status.getId())){
			this.status = status;
		}
	}

	public int getWishlistCount() {
		return wishlists.size();
	}

	public int getChatCount() {
		return 0;
	}

	public String getThumbnailUrl() {
		if(!images.getItemImages().isEmpty()) {
			return images.getThumbnail().getImageUrl();
		}

		return null;
	}

	public List<ItemImage> getImages() {
		return images.getItemImages();
	}

	public void updateUser(User user) {
		this.user = user;
	}
}
