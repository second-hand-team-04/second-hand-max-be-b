package com.codesquad.secondhand.item.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemSliceResponse {

	private boolean hasMore;
	private List<ItemResponse> items;
	
	public static ItemSliceResponse of(boolean hasNext, List<ItemResponse> itemResponses) {
		return new ItemSliceResponse(
			hasNext, itemResponses
		);
	}
}
