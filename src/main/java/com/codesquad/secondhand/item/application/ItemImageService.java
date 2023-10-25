package com.codesquad.secondhand.item.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.item.infrastructure.ItemImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImageService {

	private final ItemImageRepository itemImageRepository;

	public void deleteByItemId(Long itemId) {
		itemImageRepository.deleteAllByItemId(itemId);
	}
}
