package com.codesquad.secondhand.Image.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.common.exception.image.ImageEmptyException;
import com.codesquad.secondhand.common.exception.image.ImageInvalidTypeException;

public class ImageFileDetail {

	private static final List<String> IMAGES = List.of(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);

	private final MultipartFile multipartFile;

	public ImageFileDetail(MultipartFile multipartFile) {
		validate(multipartFile);
		this.multipartFile = multipartFile;
	}

	private void validate(MultipartFile multipartFile) {
		if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
			throw new ImageEmptyException();
		}

		if (!IMAGES.contains(multipartFile.getContentType())) {
			throw new ImageInvalidTypeException();
		}
	}

	public InputStream getInputStream() {
		try {
			return multipartFile.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getContentType() {
		return multipartFile.getContentType();
	}

	public long getContentLength() {
		return multipartFile.getSize();
	}

	public String getUploadFileName(String prefix) {
		return String.format("%s/%s", prefix, UUID.randomUUID());
	}

	public String getOriginalFileName() {
		return multipartFile.getOriginalFilename();
	}
}
