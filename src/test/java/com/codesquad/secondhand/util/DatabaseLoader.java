package com.codesquad.secondhand.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.util.fixture.CategoryFixture;
import com.codesquad.secondhand.util.fixture.ProviderFixture;
import com.codesquad.secondhand.util.fixture.RegionFixture;
import com.codesquad.secondhand.util.fixture.StatusFixture;

@Component
public class DatabaseLoader {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void initData() {
		em.createNativeQuery(CategoryFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(RegionFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(ProviderFixture.createInsertSQL()).executeUpdate();
		em.createNativeQuery(StatusFixture.createInsertSQL()).executeUpdate();
	}
}
