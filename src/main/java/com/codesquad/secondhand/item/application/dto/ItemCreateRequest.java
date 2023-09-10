package com.codesquad.secondhand.item.application.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.category.domain.Category;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemCreateRequest {

	@NotBlank(message = "제목은 공백일 수 없습니다")
	@Length(max = 60, message = "제목은 최대 60자 입니다")
	private String title;

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

	public ItemCreateRequest(String title, Integer price, String content, List<Long> imageIds, Long categoryId,
		Long regionId) {
		this.title = title;
		this.price = price;
		this.content = content;
		this.imageIds = imageIds;
		this.categoryId = categoryId;
		this.regionId = regionId;
	}

	public Item toItem(List<Image> images, Category category, Region region, Status status, User user) {
		return new Item(title, price, content, images, category, region, status, user);
	}

	public List<Long> getImageIds() {
		return Objects.isNull(imageIds) ? Collections.emptyList() : imageIds;
	}
}
