package com.codesquad.secondhand.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommonResponse<T> {

	 private final int code;
	 private final String status;
	 private final String message;
	 private final T data;

	 public static <T> CommonResponse createOK(T data, ResponseMessage responseMessage) {
		 return new CommonResponse(
			 HttpStatus.OK.value(),
			 HttpStatus.OK.getReasonPhrase(),
			 responseMessage.getMessage(),
			 data
		 );
	 }

	public static <T> CommonResponse createNoContent(T data, ResponseMessage responseMessage) {
		return new CommonResponse(
			HttpStatus.NO_CONTENT.value(),
			HttpStatus.NO_CONTENT.getReasonPhrase(),
			responseMessage.getMessage(),
			data
		);
	}

	public static <T> CommonResponse createCreated(T data, ResponseMessage responseMessage) {
		return new CommonResponse(
			HttpStatus.CREATED.value(),
			HttpStatus.CREATED.getReasonPhrase(),
			responseMessage.getMessage(),
			data
		);
	}

	public static <T> CommonResponse createNotFound(T data, ResponseMessage responseMessage) {
		return new CommonResponse(
			HttpStatus.NOT_FOUND.value(),
			HttpStatus.NOT_FOUND.getReasonPhrase(),
			responseMessage.getMessage(),
			data
		);
	}
}
