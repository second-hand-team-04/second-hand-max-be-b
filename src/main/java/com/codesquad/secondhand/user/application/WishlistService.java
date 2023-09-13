package com.codesquad.secondhand.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.user.infrastructure.WishlistRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

	private final WishlistRepository wishlistRepository;

	public void deleteByItemId(Long itemId) {
		wishlistRepository.deleteByItemId(itemId);
	}
}
