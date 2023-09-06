package com.codesquad.secondhand.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u left join fetch u.myRegions.userRegions m left join fetch m.region r where u.id = :id")
	Optional<User> findWithMyRegionsById(Long id);

	boolean existsByEmailAndProviderId(String email, Long id);

	boolean existsByNickname(String nickname);

	boolean existsByIdIsNotAndNickname(Long id, String nickname);
}
