package com.codesquad.secondhand.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.user.UserNotFoundException;
import com.codesquad.secondhand.region.application.dto.RegionResponse;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<RegionResponse> findUserRegions(Long id) {
		User user = userRepository.findWithMyRegionsById(id)
			.orElseThrow(UserNotFoundException::new);
		return RegionResponse.from(user.getRegions());
	}
}
