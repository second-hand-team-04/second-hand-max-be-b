package com.codesquad.secondhand.Image.infrastructure;

import com.codesquad.secondhand.Image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
