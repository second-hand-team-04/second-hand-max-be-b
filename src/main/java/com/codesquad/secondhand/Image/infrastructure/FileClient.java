package com.codesquad.secondhand.Image.infrastructure;

import com.codesquad.secondhand.Image.domain.ImageFileDetail;

public interface FileClient {

	String upload(ImageFileDetail imageFileDetail);
}
