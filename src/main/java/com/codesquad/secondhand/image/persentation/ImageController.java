package com.codesquad.secondhand.image.persentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.image.application.ImageFacade;
import com.codesquad.secondhand.image.application.dto.ImageUploadRequest;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageFacade imageFacade;

	@PostMapping
	public ResponseEntity<CommonResponse> upload(@RequestPart ImageUploadRequest request,
		@RequestPart MultipartFile image) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(imageFacade.upload(image),
				ResponseMessage.IMAGE_UPLOAD));
	}
}
