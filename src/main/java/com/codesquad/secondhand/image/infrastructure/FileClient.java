package com.codesquad.secondhand.image.infrastructure;

import com.codesquad.secondhand.image.domain.ImageFileDetail;

public interface FileClient {

	String upload(ImageFileDetail imageFileDetail);
}
