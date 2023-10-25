package com.codesquad.secondhand.common.exception;

import static com.codesquad.secondhand.common.response.ResponseMessage.JSON_IMPLEMENTATIONS_FAIL;
import static com.codesquad.secondhand.common.response.ResponseMessage.NO_HANDLER_FOUND;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.codesquad.secondhand.common.exception.image.MaxUploadSizeException;
import com.codesquad.secondhand.common.response.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse> methodValidHandler(MethodArgumentNotValidException e) {
		LOGGER.error("MethodArgumentNotValidException: ", e);
		String messages = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", ", "[", "]"));
		return ResponseEntity.badRequest()
			.body(CommonResponse.createBadRequest(messages));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse> customHandler(CustomException e) {
		LOGGER.error("CustomHttpException: ", e);
		return ResponseEntity.status(e.getCode())
			.body(CommonResponse.createCustomError(e));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<CommonResponse> maxUploadSizeHandler(MaxUploadSizeExceededException e) {
		LOGGER.error("MaxUploadSizeExceededException: ", e);
		return ResponseEntity.badRequest()
			.body(CommonResponse.createCustomError(new MaxUploadSizeException()));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<CommonResponse> notFoundHandler(NoHandlerFoundException e) {
		LOGGER.error("NoHandlerFoundException: ", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(CommonResponse.createNotFound(NO_HANDLER_FOUND));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponse> jsonImplementationsFailHandler(HttpMessageNotReadableException e) {
		LOGGER.error("HttpMessageNotReadableException: ", e);
		return  ResponseEntity.badRequest()
			.body(CommonResponse.createBadRequest(JSON_IMPLEMENTATIONS_FAIL.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse> exceptionHandler(Exception e) {
		LOGGER.error("Exception: ", e);
		return ResponseEntity.internalServerError()
			.body(CommonResponse.createInternalServer(e));
	}
}
