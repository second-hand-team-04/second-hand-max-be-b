package com.codesquad.secondhand.image.infrastructure;

import com.codesquad.secondhand.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
