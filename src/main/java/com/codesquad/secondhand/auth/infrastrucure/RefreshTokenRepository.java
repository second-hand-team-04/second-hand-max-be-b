package com.codesquad.secondhand.auth.infrastrucure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	void deleteByUserId(long userId);

	boolean existsByToken(String token);
}
