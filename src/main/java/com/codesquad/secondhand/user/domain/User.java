package com.codesquad.secondhand.user.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.common.exception.item.MyRegionNotIncludeException;
import com.codesquad.secondhand.common.exception.item.PermissionException;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.region.domain.Region;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selected_region_id")
	private Region selectedRegion;

	private String nickname;

	private String email;

	private String password;

	@CreatedDate
	private LocalDateTime createdAt;

	@Embedded
	private MyRegions myRegions = new MyRegions();

	@Embedded
	private MyWishlists myWishlists = new MyWishlists();

	public User(Long id, Provider provider, Image image, Region selectedRegion, String nickname, String email,
		String password, LocalDateTime createdAt) {
		this.id = id;
		this.provider = provider;
		this.image = image;
		this.selectedRegion = selectedRegion;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}

	public User(Provider provider, String nickname, String email, String password, Image image, Region region) {
		this(null, provider, image, region, nickname, email, password, null);
	}

	public List<Region> getRegions() {
		return myRegions.getRegions();
	}

	public void addMyRegion(Region region) {
		myRegions.addUserRegion(new UserRegion(this, region));
	}

	public void removeMyRegion(Region region) {
		myRegions.removeUserRegion(region);
	}

	public Account toAccount() {
		return new Account(id);
	}

	public void updateProfile(String nickname, boolean isImageChanged, Image image) {
		this.nickname = nickname;

		if (isImageChanged && Objects.isNull(image)) {
			this.image = null;
			return;
		}

		if (isImageChanged) {
			this.image = image;
		}
	}

	public void updateSelectedRegion(Region region) {
		validateNotIncludeMyRegion(region);
		this.selectedRegion = region;
	}

	public void validatePermission(User user) {
		if (!this.equals(user)) {
			throw new PermissionException();
		}
	}

	public void validateNotIncludeMyRegion(Region region) {
		if (getRegions().stream()
			.noneMatch(r -> r.equals(region))) {
			throw new MyRegionNotIncludeException();
		}
	}

	public String getImageUrl() {
		if (Objects.isNull(this.image)) {
			return null;
		}

		return image.getImageUrl();
	}

	public void addMyWishlist(Item item) {
		myWishlists.addWishList(Wishlist.of(this, item));
	}

	public void removeWishlist(Item item) {
		myWishlists.removeItem(item);
	}
}
