package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.item.domain.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
