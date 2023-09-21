package com.codesquad.secondhand.item.infrastructure;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

	@Modifying
	@Query("update Item i set i.views = i.views + 1 where i.id = :id")
	void incrementViewCount(Long id);
}
