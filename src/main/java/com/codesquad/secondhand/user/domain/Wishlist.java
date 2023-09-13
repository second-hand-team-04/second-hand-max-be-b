package com.codesquad.secondhand.user.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.item.domain.Item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	public boolean equalsUser(User user) {
		return this.user.equals(user);
	}

	public Wishlist(User user, Item item) {
		this.user = user;
		this.item = item;
	}

	public static Wishlist of(User user, Item item) {
		return new Wishlist(user, item);
	}

	public boolean equalsItem(Item item) {
		return this.item.equalsId(item.getId());
	}

	public boolean equalsItem(Long itemId) {
		return item.equalsId(itemId);
	}
}
