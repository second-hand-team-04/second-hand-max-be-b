package com.codesquad.secondhand.util;

import com.codesquad.secondhand.Image.domain.ImageFileDetail;
import com.codesquad.secondhand.Image.infrastructure.FileClient;

public class FakeS3Client implements FileClient {

	@Override
	public String upload(ImageFileDetail imageFileDetail) {
		return String.format("http://www.image.com/%s", imageFileDetail.getOriginalFileName());
	}
}
