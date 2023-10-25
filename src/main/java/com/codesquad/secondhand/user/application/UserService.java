package com.codesquad.secondhand.user.application;

import static com.codesquad.secondhand.common.util.CacheType.CacheName.MY_REGION;
import static com.codesquad.secondhand.common.util.CacheType.CacheName.WISH_ITEM;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.user.UserEmailAndProviderDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNicknameDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNotFoundException;
import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.MyRegionResponse;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

import com.codesquad.secondhand.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public User signUp(UserCreateRequest request, Provider provider, Image image, Region region) {
		validateDuplication(request, provider.getId());
		User user = userRepository.save(request.toUser(provider, image, region));
		user.addMyRegion(region);
		return user;
	}

	private void validateDuplication(UserCreateRequest request, Long providerId) {
		if (userRepository.existsByEmailAndProviderId(request.getEmail(), providerId)) {
			throw new UserEmailAndProviderDuplicationException();
		}

		if (userRepository.existsByNickname(request.getNickname())) {
			throw new UserNicknameDuplicationException();
		}
	}

	@Transactional
	public void updateProfile(UserUpdateRequest request, Image image) {
		validateDuplicationNicknameWithDifferentId(request.getId(), request.getNickname());
		User user = findByIdOrThrow(request.getId());
		user.updateProfile(request.getNickname(), request.getIsImageChanged(), image);
	}

	private void validateDuplicationNicknameWithDifferentId(Long id, String nickname) {
		if (userRepository.existsByIdIsNotAndNickname(id, nickname)) {
			throw new UserNicknameDuplicationException();
		}
	}

	public User findByIdOrThrow(Long id) {
		return userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
	}

	@Cacheable(cacheNames = MY_REGION, key = "#id")
	public MyRegionResponse findUserRegions(Long id) {
		User user = userRepository.findWithMyRegionsById(id)
			.orElseThrow(UserNotFoundException::new);
		List<RegionResponse> regionResponses = RegionResponse.from(user.getRegions());
		return MyRegionResponse.of(user.getSelectedRegion(), regionResponses);
	}

	@Transactional
	@CacheEvict(cacheNames = MY_REGION, key = "#userId")
	public void addMyRegion(Long userId, Region region) {
		User user = findByIdOrThrow(userId);
		user.addMyRegion(region);
	}

	@Transactional
	@CacheEvict(cacheNames = MY_REGION, key = "#userId")
	public void removeMyRegion(Long userId, Region region) {
		User user = findByIdOrThrow(userId);
		user.removeMyRegion(region);
	}

	@Transactional
	@CacheEvict(cacheNames = WISH_ITEM, key = "#item.id")
	public void addMyWishlist(Long id, Item item) {
		User user = findByIdOrThrow(id);
		user.addMyWishlist(item);
	}

	@Transactional
	@CacheEvict(cacheNames = WISH_ITEM, key = "#item.id")
	public void removeMyWishlist(Long id, Item item) {
		User user = findByIdOrThrow(id);
		user.removeWishlist(item);
	}

	@Cacheable(cacheNames = WISH_ITEM, key = "#itemId")
	public WishItem findWishItem(Long itemId) {
		return userRepository.findRedisWishItemByItemId(itemId)
			.orElseGet(() -> userRepository.findWishItemByItemId(itemId));
	}
}
