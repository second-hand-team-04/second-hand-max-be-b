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

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.region.domain.Region;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

	private String nickname;

	private String email;

	private String password;

	@CreatedDate
	private LocalDateTime createdAt;

	@Embedded
	private MyRegions myRegions = new MyRegions();

	public User(Long id, Provider provider, Image image, String nickname, String email, String password,
		LocalDateTime createdAt) {
		this.id = id;
		this.provider = provider;
		this.image = image;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}

	public User(Provider provider, String nickname, String email, String password, Image image) {
		this.provider = provider;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.image = image;
	}

	public List<Region> getRegions() {
		return myRegions.getRegions();
	}

	public void addMyRegion(Region region) {
		myRegions.addUserRegion(new UserRegion(this, region));
	}

	public void removeMyRegion(Long regionId) {
		myRegions.removeUserRegion(regionId);
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

	public boolean equalsId(Long id) {
		return Objects.equals(this.id, id);
	}

	public Long getId() {
		return id;
	}

	public Provider getProvider() {
		return provider;
	}

	public String getNickname() {
		return nickname;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Image getImage() {
		return image;
	}

	public String getImageUrl() {
		if(Objects.isNull(this.image)){
			return null;
		}

		return image.getImageUrl();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
