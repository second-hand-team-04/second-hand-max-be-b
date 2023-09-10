package com.codesquad.secondhand.item.infrastructure;

import java.util.Optional;

import javax.persistence.LockModeType;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select i from Item i inner join fetch i.region r "
		+ "inner join fetch i.category c "
		+ "inner join fetch i.status s "
		+ "inner join fetch i.user u "
		+ "left join fetch i.images.itemImages ii "
		+ "left join fetch ii.image image "
		+ "where i.id = :id and i.isDeleted = false")
	Optional<Item> findDetailById(Long id);

	@Query("select i from Item i inner join fetch i.region r left join fetch i.images.itemImages ii left join fetch ii.image im where i.user.id = :userId and i.status.id in :statusIds and i.isDeleted = false order by i.updatedAt desc, im.id asc")
	Slice<Item> findByUserAndStatusIn(@Param("userId") Long userId, @Param("statusIds") List<Long> statusIds,
		Pageable pageable);
}
