package com.codesquad.secondhand.image.application.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.codesquad.secondhand.image.domain.Image;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageResponse implements Serializable {

	private Long id;
	private String imageUrl;

	public static List<ImageResponse> from(List<Image> images) {
		return images.stream()
			.map(ImageResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}

	public static ImageResponse from(Image image) {
		return new ImageResponse(image.getId(), image.getImageUrl());
	}
}
