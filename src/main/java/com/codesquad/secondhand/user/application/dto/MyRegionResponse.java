package com.codesquad.secondhand.user.application.dto;

import java.util.List;

import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyRegionResponse {

	private Long selectedId;
	private List<RegionResponse> regions;

	public static MyRegionResponse of(Region selectedRegion, List<RegionResponse> regionResponses) {
		return new MyRegionResponse(selectedRegion.getId(), regionResponses.isEmpty() ? null : regionResponses);
	}
}
