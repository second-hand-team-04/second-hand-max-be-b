package com.codesquad.secondhand.item.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTransactionSliceResponse {

	private boolean hasMore;
	private List<MyTransactionReponse> items;

	public static MyTransactionSliceResponse of(boolean hasMore, List<MyTransactionReponse> myTransactionReponses) {
		return new MyTransactionSliceResponse(
			hasMore, myTransactionReponses
		);
	}
}
