package com.codesquad.secondhand.util.fixture;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import com.codesquad.secondhand.region.domain.Region;

public enum RegionFixture {

	동네_서울_종로구_청운동(1L, "서울특별시 종로구 청운동"),
	동네_서울_종로구_신교동(2L, "서울특별시 종로구 신교동"),
	동네_서울_종로구_궁정동(3L, "서울특별시 종로구 궁정동"),
	동네_서울_종로구_효자동(4L, "서울특별시 종로구 효자동"),
	동네_서울_종로구_창성동(5L, "서울특별시 종로구 창성동"),
	동네_서울_종로구_통의동(6L, "서울특별시 종로구 통의동"),
	동네_서울_종로구_적선동(7L, "서울특별시 종로구 적선동"),
	동네_서울_종로구_통인동(8L, "서울특별시 종로구 통인동"),
	동네_서울_종로구_누상동(9L, "서울특별시 종로구 누상동"),
	동네_서울_종로구_누하동(10L, "서울특별시 종로구 누하동"),
	동네_서울_종로구_옥인동(11L, "서울특별시 종로구 옥인동"),
	동네_서울_종로구_체부동(12L, "서울특별시 종로구 체부동"),
	동네_서울_종로구_필운동(13L, "서울특별시 종로구 필운동"),
	동네_서울_종로구_내자동(14L, "서울특별시 종로구 내자동"),
	동네_서울_종로구_사직동(15L, "서울특별시 종로구 사직동"),
	동네_서울_종로구_도렴동(16L, "서울특별시 종로구 도렴동"),
	동네_서울_종로구_당주동(17L, "서울특별시 종로구 당주동"),
	동네_서울_종로구_내수동(18L, "서울특별시 종로구 내수동"),
	동네_서울_종로구_세종로(19L, "서울특별시 종로구 세종로"),
	동네_서울_종로구_신문로1가(20L, "서울특별시 종로구 신문로1가"),
	동네_서울_종로구_신문로2가(21L, "서울특별시 종로구 신문로2가");

	private final Long id;
	private final String title;

	RegionFixture(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO region(title) VALUES %s",
			Arrays.stream(values())
				.map(r -> String.format(
					"('%s')",
					r.getTitle()))
				.collect(Collectors.joining(", ")));
	}

	public static RegionFixture findById(Long id) {
		return Arrays.stream(values())
			.filter(ur -> Objects.equals(ur.getId(), id))
			.findAny()
			.orElseThrow();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Region getRegion() {
		return new Region(id, title);
	}
}
