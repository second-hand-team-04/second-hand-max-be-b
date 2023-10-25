package com.codesquad.secondhand.item.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemUpdateStatusRequest {

	private Long id;
	private Long userId;
	private Long status;

	public ItemUpdateStatusRequest(Long status) {
		this.status = status;
	}

	public void injectIdAndUserId(Long id, Long userId) {
		this.id = id;
		this.userId = userId;
	}
}
