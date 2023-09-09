package com.codesquad.secondhand.item.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.common.exception.image.ImageNotFoundException;
import com.codesquad.secondhand.common.exception.item.ItemImageMaxAddCountException;

@Embeddable
public class Images {

	private static final int MAX_ADD_IMAGE_COUNT = 10;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemImage> itemImages = new ArrayList<>();
  
	public String getThumbnailUrl() {
		if (itemImages.isEmpty()) {
			return null;
		}

		return itemImages.stream()
			.findFirst()
			.map(ItemImage::getImage)
			.orElseThrow(ImageNotFoundException::new)
			.getImageUrl();
	}

	public void addImage(List<ItemImage> itemImages) {
		validateMaxAddImageCount(itemImages.size());
		this.itemImages.addAll(itemImages);
	}

	public void addImage(ItemImage itemImage) {
		validateMaxAddImageCount(1);
		this.itemImages.add(itemImage);
	}

	public List<Image> getImages() {
		return itemImages.stream()
			.map(ItemImage::getImage)
			.collect(Collectors.toUnmodifiableList());
	}

	private void validateMaxAddImageCount(int size) {
		if (size >= MAX_ADD_IMAGE_COUNT) {
			throw new ItemImageMaxAddCountException();
		}
	}
}
