package com.codesquad.secondhand.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.user.UserNotFoundException;
import com.codesquad.secondhand.region.application.RegionService;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RegionService regionService;

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
