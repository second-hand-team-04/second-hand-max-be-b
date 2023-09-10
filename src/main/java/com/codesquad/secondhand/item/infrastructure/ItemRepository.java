package com.codesquad.secondhand.item.infrastructure;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemDao {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select i from Item i inner join fetch i.region r "
		+ "inner join fetch i.category c "
		+ "inner join fetch i.status s "
		+ "inner join fetch i.user u "
		+ "left join fetch i.images.itemImages ii "
		+ "left join fetch ii.image image "
		+ "where i.id = :id")
	Optional<Item> findDetailById(Long id);
}
