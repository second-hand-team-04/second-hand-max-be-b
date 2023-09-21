package com.codesquad.secondhand.util;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

public abstract class DockerContainer {

	private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.34")
		.withDatabaseName("test")
		.withUsername("root")
		.withPassword("root");

	private static final GenericContainer REDIS_CACHE_CONTAINER = new GenericContainer<>("redis:7.2.1")
		.withExposedPorts(6379);

	static {
		MY_SQL_CONTAINER.start();
		REDIS_CACHE_CONTAINER.start();
		System.setProperty("spring.redis.host", REDIS_CACHE_CONTAINER.getHost());
		System.setProperty("spring.redis.port", REDIS_CACHE_CONTAINER.getMappedPort(6379).toString());
		System.setProperty("spring.redis.password", "");
	}
}
