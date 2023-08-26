package com.codesquad.secondhand.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codesquad.secondhand.common.response.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse> customHandler(CustomException e) {
		LOGGER.error("CustomHttpException: ", e);
		return ResponseEntity.status(e.getCode())
			.body(CommonResponse.createCustomError(e));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse> exceptionHandler(Exception e) {
		LOGGER.error("Exception: ", e);
		return ResponseEntity.internalServerError()
			.body(CommonResponse.createInternalServer(e));
	}
}
