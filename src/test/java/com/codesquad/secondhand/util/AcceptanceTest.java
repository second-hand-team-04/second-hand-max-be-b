package com.codesquad.secondhand.util;

import static com.codesquad.secondhand.util.fixture.ProviderFixture.공급자_내부;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static com.codesquad.secondhand.util.steps.AuthSteps.로그인_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.유저_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("classpath:schema.sql")
public abstract class AcceptanceTest extends DockerContainer {

	protected static final String PROFILE_PATH = String.format("%s/%s", System.getProperty("user.dir"),
		"src/test/resources/bike.jpg");

	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseLoader databaseLoader;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	protected String 유저_만두_액세스_토큰;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseLoader.initData();
		유저_생성_요청(공급자_내부.getId(), 유저_만두.getEmail(), 유저_만두.getNickname(), 유저_만두.getPassword(), null);
		유저_만두_액세스_토큰 = 로그인_요청(유저_만두.getEmail(), 유저_만두.getPassword()).jsonPath().getString("data.accessToken");
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}

	protected void 응답_상태코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}

	protected MultiPartSpecification createFile(String fileName) {
		try {
			return new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
				.fileName(URLEncoder.encode(fileName, StandardCharsets.UTF_8))
				.controlName("image")
				.mimeType(MediaType.IMAGE_JPEG_VALUE)
				.build();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
