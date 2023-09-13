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

import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.domain.Wishlist;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
		user.validateNotIncludeMyRegion(region);
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

	public void increaseViewCount() {
		views++;
	}

	public void updateStatus(User accountUser, Status status) {
		this.user.validatePermission(accountUser);
		this.status = status;
	}

	public void update(String title, Integer price, String content, User accountUser, List<Image> images,
		Category category, Region region) {
		this.user.validatePermission(accountUser);
		this.user.validateNotIncludeMyRegion(region);
		this.title = title;
		this.price = price;
		this.content = content;
		this.category = category;
		this.region = region;
		updateImage(images);
	}

	private void updateImage(List<Image> images) {
		if (Objects.isNull(images) || images.isEmpty()) {
			this.images.clear();
			return;
		}

		List<Image> originalImages = this.images.getImages();
		removeImagesForUpdate(images, originalImages);
		addImagesForUpdate(images, originalImages);
	}

	private void addImagesForUpdate(List<Image> images, List<Image> originalImages) {
		List<Image> addImages = images.stream()
			.filter(i -> !originalImages.contains(i))
			.collect(Collectors.toUnmodifiableList());
		addImage(addImages);
	}

	private void removeImagesForUpdate(List<Image> images, List<Image> originalImages) {
		List<Image> removeImages = originalImages.stream()
			.filter(i -> !images.contains(i))
			.collect(Collectors.toUnmodifiableList());
		this.images.removeAll(removeImages);
	}

	public void delete(User accountUser) {
		this.user.validatePermission(accountUser);
		this.isDeleted = true;
	}

	public int getWishlistCount() {
		return wishlists.size();
	}

	public boolean isMyWishlisted(User user) {
		return wishlists.stream()
			.anyMatch(w -> w.equalsUser(user));
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

	public boolean equalsId(Long id) {
		return Objects.equals(this.id, id);
	}

	public Long getSellerId() {
		return user.getId();
	}
}
