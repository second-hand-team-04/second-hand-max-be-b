package com.codesquad.secondhand.item.application.dto;

import java.io.Serializable;

import com.codesquad.secondhand.item.domain.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StatusItemDetailResponse implements Serializable {

	private String status;

	public static StatusItemDetailResponse from(Status status) {
		return new StatusItemDetailResponse(status.getType());
	}
}
