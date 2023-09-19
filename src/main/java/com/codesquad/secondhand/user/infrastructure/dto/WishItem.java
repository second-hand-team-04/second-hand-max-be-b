package com.codesquad.secondhand.user.infrastructure.dto;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class WishItem implements Serializable {

	private int numLike;
	private boolean isLiked;

	public WishItem(int numLike, boolean isLiked) {
		this.numLike = numLike;
		this.isLiked = isLiked;
	}

	public WishItem(long numLike, long isLiked) {
		this.numLike = (int) numLike;
		this.isLiked = isLiked > 0;
	}
}
