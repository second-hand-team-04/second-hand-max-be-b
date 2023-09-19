package com.codesquad.secondhand.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.infrastructure.dto.WishItem;

public interface UserRepository extends JpaRepository<User, Long>, WishlistCustomRepository {

	@Query("select u from User u left join fetch u.myRegions.userRegions m left join fetch m.region r where u.id = :id order by r.title")
	Optional<User> findWithMyRegionsById(Long id);

	boolean existsByEmailAndProviderId(String email, Long id);

	boolean existsByNickname(String nickname);

	boolean existsByIdIsNotAndNickname(Long id, String nickname);

	@Query("select new com.codesquad.secondhand.user.infrastructure.dto.WishItem(count(w.id),  COALESCE(sum(case when w.user.id = :id then 0 else 1 end), 0)) from Wishlist w where w.item.id = :itemId")
	Optional<WishItem> findWishItemByIdAndItemId(Long id, Long itemId);
}
