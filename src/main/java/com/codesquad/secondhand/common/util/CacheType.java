package com.codesquad.secondhand.common.util;

public enum CacheType {

	CATEGORY(CacheName.CATEGORY),
	ITEM(CacheName.ITEM),
	ITEM_VIEW_COUNT(CacheName.ITEM_VIEW_COUNT),
	MY_REGION(CacheName.MY_REGION),
	WISH_ITEM(CacheName.WISH_ITEM);

	private final String cacheType;

	CacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	public String getCacheType() {
		return cacheType;
	}

	public static class CacheName {

		public static final String CATEGORY = "category";
		public static final String ITEM = "item";
		public static final String ITEM_VIEW_COUNT = "itemViewCount";
		public static final String MY_REGION = "myRegion";
		public static final String WISH_ITEM = "wishItem";
	}
}
