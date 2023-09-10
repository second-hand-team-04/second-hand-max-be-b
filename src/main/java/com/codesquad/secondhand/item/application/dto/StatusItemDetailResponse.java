package com.codesquad.secondhand.item.application.dto;

import com.codesquad.secondhand.item.domain.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatusItemDetailResponse {

	private String status;

	public static StatusItemDetailResponse from(Status status) {
		return new StatusItemDetailResponse(status.getType());
	}
}
