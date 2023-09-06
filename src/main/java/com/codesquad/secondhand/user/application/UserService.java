package com.codesquad.secondhand.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codesquad.secondhand.Image.application.ImageService;
import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.common.exception.user.ProviderNotFoundException;
import com.codesquad.secondhand.common.exception.user.UserEmailAndProviderDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNicknameDuplicationException;
import com.codesquad.secondhand.common.exception.user.UserNotFoundException;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.infrastructure.ProviderRepository;
import com.codesquad.secondhand.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ProviderRepository providerRepository;
	private final RegionService regionService;
	private final ImageService imageService;

	@Transactional
	public User signUp(UserCreateRequest request, MultipartFile profileImage) {
		validateDuplication(request, ProviderType.LOCAL.getId());
		Provider provider = providerRepository.findById(request.getProviderId())
			.orElseThrow(ProviderNotFoundException::new);
		Image image = imageService.uploadOrElseNull(profileImage);
		User user = userRepository.save(request.toUser(provider, image));
		user.addMyRegion(regionService.findByIdOrThrow(Region.YEOKSAM_DONG));
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
	public void updateProfile(UserUpdateRequest request, MultipartFile profileImage) {
		validateDuplicationNicknameWithDifferentId(request.getId(), request.getNickname());
		Image image = imageService.uploadOrElseNull(profileImage);
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
	public void addMyRegion(UserRegionAddRequest request) {
		User user = findByIdOrThrow(request.getUserId());
		Region region = regionService.findByIdOrThrow(request.getRegionId());
		user.addMyRegion(region);
	}

	@Transactional
	public void removeMyRegion(Long userId, Long regionId) {
		User user = findByIdOrThrow(userId);
		user.removeMyRegion(regionId);
	}
}
