package com.codesquad.secondhand.image.application.dto;

import com.codesquad.secondhand.image.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {

	private Long id;
	private String imageUrl;

	public static ImageUploadResponse from(Image image) {
		return new ImageUploadResponse(
			image.getId(),
			image.getImageUrl()
		);
	}
}
