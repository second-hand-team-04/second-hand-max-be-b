package com.codesquad.secondhand.item.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.wishlist.domain.Wishlist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@OneToMany(mappedBy = "item")
	private List<Wishlist> wishlists = new ArrayList<>();

	private String title;

	private String content;

	private Integer price;

	private String status;

	private Integer views;

	private LocalDateTime postedAt;

	private LocalDateTime updatedAt;

	private Boolean isDeleted;

	public int getLikeCount() {
		return wishlists.size();
	}

	public int getChatCount() {
		return 0;
	}
}
