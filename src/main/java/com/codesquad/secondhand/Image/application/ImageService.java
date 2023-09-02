package com.codesquad.secondhand.Image.application;

import static com.codesquad.secondhand.Image.domain.Image.ITEM_DEFAULT_IMAGE_ID;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.Image.domain.ImageFileDetail;
import com.codesquad.secondhand.Image.infrastructure.ImageRepository;
import com.codesquad.secondhand.Image.infrastructure.S3Client;
import com.codesquad.secondhand.common.exception.image.ImageNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final S3Client s3Client;
	private Image itemDefaultImage;

	@Transactional
	public Image upload(MultipartFile profilePicture) {
		String ImageUrl = s3Client.upload(new ImageFileDetail(profilePicture));
		return imageRepository.save(new Image(ImageUrl));
	}

	public Image getItemDefaultImage() {
		if (Objects.isNull(itemDefaultImage)) {
			itemDefaultImage = imageRepository.findById(ITEM_DEFAULT_IMAGE_ID)
				.orElseThrow(ImageNotFoundException::new);
		}

		return itemDefaultImage;
	}
}
