package com.codesquad.secondhand.auth.infrastrucure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.user.domain.User;

public interface AuthRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndPasswordAndProviderId(String email, String password, Long providerId);

	Optional<User> findByEmailAndProviderId(String email, Long providerId);
}
