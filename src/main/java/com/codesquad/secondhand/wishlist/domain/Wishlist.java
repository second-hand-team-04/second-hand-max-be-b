package com.codesquad.secondhand.wishlist.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.user.domain.User;

@Entity
public class Wishlist {
	@Id
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	Item item;
}
