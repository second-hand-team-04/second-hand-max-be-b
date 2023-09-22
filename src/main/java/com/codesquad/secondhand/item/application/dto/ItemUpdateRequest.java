package com.codesquad.secondhand.item.application.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemUpdateRequest {

	private Long id;

	private Long userId;

	@NotBlank(message = "제목은 공백일 수 없습니다")
	@Length(max = 60, message = "제목은 최대 60자 입니다")
	private String title;

	@PositiveOrZero(message = "음수는 등록할 수 없습니다")
	@Max(value = 999_999_999, message = "등록 가능한 금액 한도를 초과했습니다")
	private Integer price;

	@NotBlank(message = "내용은 공백일 수 없습니다")
	@Length(max = 3000, message = "내용은 최대 3000자 입니다")
	private String content;

	@Size(max = 10, message = "상품 이미지 등록 수가 최대 제한을 초과했습니다. 상품 등록 요청이 거부되었습니다")
	private List<Long> imageIds;

	@NotNull(message = "상품 카테고리가 선택되지 않았습니다")
	private Long categoryId;

	@NotNull(message = "지역이 선택되지 않았습니다")
	private Long regionId;

	public ItemUpdateRequest(String title, Integer price, String content, List<Long> imageIds, Long categoryId,
		Long regionId) {
		this.title = title;
		this.price = price;
		this.content = content;
		this.imageIds = imageIds;
		this.categoryId = categoryId;
		this.regionId = regionId;
	}

	public void injectIdAndUserId(Long id, Long userId) {
		this.id = id;
		this.userId = userId;
	}

	public List<Long> getImageIds() {
		return Objects.isNull(imageIds) ? Collections.emptyList() : imageIds;
	}
}
