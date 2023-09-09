package com.codesquad.secondhand.Image.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.Image.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageResponse {

	private Long id;
	private String url;

	public static List<ImageResponse> from(List<Image> images) {
		return images.stream()
			.map(ImageResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}

	public static ImageResponse from(Image image) {
		return new ImageResponse(image.getId(), image.getImageUrl());
	}
}
