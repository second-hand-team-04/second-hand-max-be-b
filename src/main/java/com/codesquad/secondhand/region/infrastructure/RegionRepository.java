package com.codesquad.secondhand.region.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.region.domain.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

	Slice<Region> findSliceByTitleContainsOrderByTitle(Pageable pageable, String title);
}
