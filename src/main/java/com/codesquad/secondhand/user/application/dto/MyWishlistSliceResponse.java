package com.codesquad.secondhand.user.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyWishlistSliceResponse {

	private boolean hasMore;

	private List<MyWishlistResponse> items;

	public static MyWishlistSliceResponse of(boolean hasMore, List<MyWishlistResponse> myWishlistResponses) {
		return new MyWishlistSliceResponse(hasMore, myWishlistResponses);
	}
}
