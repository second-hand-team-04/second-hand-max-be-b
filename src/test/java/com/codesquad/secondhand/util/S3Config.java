package com.codesquad.secondhand.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codesquad.secondhand.Image.infrastructure.FileClient;

@Configuration
public class S3Config {

	@Bean
	public FileClient fileClient() {
		return new FakeS3Client();
	}
}
