package com.codesquad.secondhand.item.infrastructure.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.codesquad.secondhand.category.application.dto.CategoryInfoResponse;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.application.dto.StatusItemDetailResponse;
import com.codesquad.secondhand.user.application.dto.UserItemDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemDetailDto implements Serializable {

	private Long id;
	private String title;
	private String content;
	private Integer price;
	private LocalDateTime updatedAt;
	private StatusItemDetailResponse status;
	private CategoryInfoResponse category;
	private UserItemDetailResponse seller;
	private List<Image> images;
}
