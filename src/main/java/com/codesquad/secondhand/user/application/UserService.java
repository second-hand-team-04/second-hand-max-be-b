package com.codesquad.secondhand.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.common.exception.user.UserEmailAndProviderDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNicknameDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNotFoundException;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
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
		User user = userRepository.save(request.toUser(provider, image));
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

	public List<RegionResponse> findUserRegions(Long id) {
		User user = userRepository.findWithMyRegionsById(id)
			.orElseThrow(UserNotFoundException::new);
		return RegionResponse.from(user.getRegions());
	}

	@Transactional
	public void addMyRegion(UserRegionAddRequest request, Region region) {
		User user = findByIdOrThrow(request.getUserId());
		user.addMyRegion(region);
	}

	@Transactional
	public void removeMyRegion(Long userId, Region region) {
		User user = findByIdOrThrow(userId);
		user.removeMyRegion(region);
	}
}
