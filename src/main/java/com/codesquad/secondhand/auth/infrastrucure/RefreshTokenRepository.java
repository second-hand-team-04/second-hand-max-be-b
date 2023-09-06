package com.codesquad.secondhand.auth.infrastrucure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	void deleteByUserId(long userId);

	Optional<RefreshToken> findByUserId(Long userId);
}
