package com.codesquad.secondhand.Image.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.Image.domain.ImageFileDetail;
import com.codesquad.secondhand.Image.infrastructure.FileClient;
import com.codesquad.secondhand.Image.infrastructure.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final FileClient fileClient;

	@Transactional
	public Image upload(MultipartFile multipartFiles) {
		String imageUrl = fileClient.upload(new ImageFileDetail(multipartFiles));
		return imageRepository.save(new Image(imageUrl));
	}

	@Transactional
	public List<Image> upload(MultipartFile... multipartFiles) {
		List<Image> images = new ArrayList<>();
		Arrays.stream(multipartFiles)
			.forEach(m -> {
				String imageUrl = fileClient.upload(new ImageFileDetail(m));
				images.add(new Image(imageUrl));
			});
		return imageRepository.saveAll(images);
	}
}
