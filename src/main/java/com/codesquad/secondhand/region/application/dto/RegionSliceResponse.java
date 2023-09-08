package com.codesquad.secondhand.region.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegionSliceResponse {

	private boolean hasMore;
	private List<RegionResponse> regions;

	public static RegionSliceResponse of(boolean hasNext, List<RegionResponse> regionResponses) {
		return new RegionSliceResponse(
			hasNext,
			regionResponses
		);
	}
}
