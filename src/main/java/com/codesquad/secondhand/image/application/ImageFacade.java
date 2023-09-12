package com.codesquad.secondhand.image.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.image.application.dto.ImageUploadResponse;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class ImageFacade {

	private final ImageService imageService;

	public ImageUploadResponse upload(MultipartFile multipartFile) {
		return ImageUploadResponse.from(imageService.upload(multipartFile));
	}
}
