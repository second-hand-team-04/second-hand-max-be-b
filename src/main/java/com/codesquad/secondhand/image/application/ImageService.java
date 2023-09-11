package com.codesquad.secondhand.image.application;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.image.domain.ImageFileDetail;
import com.codesquad.secondhand.image.infrastructure.FileClient;
import com.codesquad.secondhand.image.infrastructure.ImageRepository;
import com.codesquad.secondhand.common.exception.image.ImageNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final FileClient fileClient;

	public Image upload(MultipartFile multipartFiles) {
		String imageUrl = fileClient.upload(new ImageFileDetail(multipartFiles));
		return imageRepository.save(new Image(imageUrl));
	}

	public Image uploadOrElseNull(MultipartFile profilePicture) {
		if (Objects.nonNull(profilePicture)) {
			return upload(profilePicture);
		}

		return null;
	}

	public List<Image> findAllByIdOrThrow(List<Long> imageIds) {
		if (imageIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<Image> images = imageRepository.findAllById(imageIds);

		if(images.isEmpty()){
			throw new ImageNotFoundException();
		}

		return images;
	}

	public Image findByIdOrThrow(Long id) {
		if (Objects.isNull(id)) {
			throw new ImageNotFoundException();
		}
		return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
	}
}
