package com.codesquad.secondhand.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.util.fixture.CategoryFixture;
import com.codesquad.secondhand.util.fixture.ImageFixture;
import com.codesquad.secondhand.util.fixture.ItemFixture;
import com.codesquad.secondhand.util.fixture.ItemImageFixture;
import com.codesquad.secondhand.util.fixture.RegionFixture;
import com.codesquad.secondhand.util.fixture.StatusFixture;
import com.codesquad.secondhand.util.fixture.UserFixture;
import com.codesquad.secondhand.util.fixture.WishlistFixture;

@Component
public class DatabaseLoader {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void loadData() {
		em.createNativeQuery(CategoryFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(RegionFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(ImageFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(UserFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(StatusFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(ItemFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(ItemImageFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(WishlistFixture.createInsertSQL()).executeUpdate();
	}
}
