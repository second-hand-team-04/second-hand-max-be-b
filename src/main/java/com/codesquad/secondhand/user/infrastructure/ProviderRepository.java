package com.codesquad.secondhand.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.user.domain.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
