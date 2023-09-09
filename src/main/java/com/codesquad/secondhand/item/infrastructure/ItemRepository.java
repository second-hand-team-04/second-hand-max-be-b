package com.codesquad.secondhand.item.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codesquad.secondhand.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemDao {
}
