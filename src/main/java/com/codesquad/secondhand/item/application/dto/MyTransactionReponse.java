package com.codesquad.secondhand.item.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.item.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyTransactionReponse {
	private Long id;
	private String title;
	private String region;
	private LocalDateTime updatedAt;
	private Integer price;
	private String thumbnail;

	public static MyTransactionReponse of(Item item) {
		return new MyTransactionReponse(
			item.getId(),
			item.getTitle(),
			item.getRegion().getTitle(),
			item.getUpdatedAt(),
			item.getPrice(),
			item.getThumbnailUrl()
		);
	}

	public static List<MyTransactionReponse> of(List<Item> items) {
		return items.stream()
			.map(MyTransactionReponse::of)
			.collect(Collectors.toUnmodifiableList());
	}
}
