package com.codesquad.secondhand.util;

import com.codesquad.secondhand.image.domain.ImageFileDetail;
import com.codesquad.secondhand.image.infrastructure.FileClient;

public class FakeS3Client implements FileClient {

	@Override
	public String upload(ImageFileDetail imageFileDetail) {
		return String.format("http://www.image.com/%s", imageFileDetail.getOriginalFileName());
	}
}
