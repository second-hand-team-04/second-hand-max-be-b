package com.codesquad.secondhand.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.user.ProviderNotFoundException;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.infrastructure.ProviderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProviderService {

	private final ProviderRepository providerRepository;

	public Provider findByIdOrElseThrow(Long id) {
		return providerRepository.findById(id)
			.orElseThrow(ProviderNotFoundException::new);
	}
}
