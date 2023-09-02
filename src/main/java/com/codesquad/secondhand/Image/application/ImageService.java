package com.codesquad.secondhand.Image.application;

import static com.codesquad.secondhand.Image.domain.Image.ITEM_DEFAULT_IMAGE_ID;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.Image.infrastructure.ImageRepository;
import com.codesquad.secondhand.common.exception.image.ImageNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private Image itemDefaultImage;

	public Image getItemDefaultImage() {
		if (Objects.nonNull(itemDefaultImage)) {
			return itemDefaultImage;
		}

		return imageRepository.findById(ITEM_DEFAULT_IMAGE_ID)
			.orElseThrow(ImageNotFoundException::new);
	}
}
