package com.codesquad.secondhand.Image.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {

	public static final long USER_PROFILE_DEFAULT_IMAGE_ID = 1L;
	public static final long ITEM_DEFAULT_IMAGE_ID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageUrl;
}
