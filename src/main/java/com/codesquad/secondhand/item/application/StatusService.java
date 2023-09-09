package com.codesquad.secondhand.item.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.common.exception.item.StatusNotFoundException;
import com.codesquad.secondhand.item.domain.Status;
import com.codesquad.secondhand.item.infrastructure.StatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatusService {

	private final StatusRepository statusRepository;

	public Status findByIdOrThrow(Long id) {
		return statusRepository.findById(id).orElseThrow(StatusNotFoundException::new);
	}
}
