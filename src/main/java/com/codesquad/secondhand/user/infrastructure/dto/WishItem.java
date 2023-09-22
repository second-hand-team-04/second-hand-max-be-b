package com.codesquad.secondhand.user.infrastructure.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WishItem implements Serializable {

	private List<Long> wishUserId;

	public WishItem(List<Long> wishUserId) {
		this.wishUserId = wishUserId;
	}

	public static WishItem createNonNullInstance(WishItem wishItem) {
		return Objects.isNull(wishItem) ? new WishItem(Collections.emptyList()) : wishItem;
	}

	public int getNumLike() {
		return wishUserId.size();
	}

	public boolean isLiked(Long userId) {
		return wishUserId.stream()
			.anyMatch(u -> u.equals(userId));
	}
}
