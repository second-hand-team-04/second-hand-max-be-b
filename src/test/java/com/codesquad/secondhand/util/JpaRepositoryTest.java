package com.codesquad.secondhand.util;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JpaRepositoryTest extends MySqlContainer {

	@Autowired
	private DatabaseLoader databaseLoader;

	@BeforeEach
	void setUp() {
		databaseLoader.loadData();
	}
}
