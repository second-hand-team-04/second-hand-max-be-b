package com.codesquad.secondhand.region.application.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.region.domain.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegionResponse implements Serializable {

	private Long id;
	private String title;

	public static RegionResponse from(Region region) {
		return new RegionResponse(
			region.getId(),
			region.getTitle()
		);
	}

	public static List<RegionResponse> from(List<Region> regions) {
		return regions.stream()
			.map(RegionResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
