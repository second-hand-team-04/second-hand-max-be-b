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
	private static final int MIN_REMOVE_COUNT = 1;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemImage> itemImages = new ArrayList<>();

	public Image getThumbnail() {
		if (itemImages.isEmpty()) {
			return null;
		}

		return itemImages.stream()
			.findFirst()
			.map(ItemImage::getImage)
			.orElseThrow(ImageNotFoundException::new);
	}

	public void addItemImage(List<ItemImage> itemImages) {
		validateMaxAddImageCount(itemImages);
		this.itemImages.addAll(itemImages);
	}

	public void addItemImage(ItemImage itemImage) {
		validateMaxAddImageCount();
		this.itemImages.add(itemImage);
	}

	public List<Image> getImages() {
		return itemImages.stream()
			.map(ItemImage::getImage)
			.collect(Collectors.toUnmodifiableList());
	}

	public List<ItemImage> getItemImages() {
		return itemImages;
	}

	private void validateMaxAddImageCount() {
		if (this.itemImages.size() >= MAX_ADD_IMAGE_COUNT) {
			throw new ItemImageMaxAddCountException();
		}
	}

	private void validateMaxAddImageCount(List<ItemImage> itemImages) {
		if (this.itemImages.size() + itemImages.size() > MAX_ADD_IMAGE_COUNT) {
			throw new ItemImageMaxAddCountException();
		}
	}
}
