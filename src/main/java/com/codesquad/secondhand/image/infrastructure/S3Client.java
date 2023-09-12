package com.codesquad.secondhand.image.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codesquad.secondhand.image.domain.ImageFileDetail;

@Component
public class S3Client implements FileClient {

	private static final String BUCKET_FOLDER = "images";

	private final String bucketName;
	private final AmazonS3 amazonS3;

	public S3Client(@Value("${cloud.aws.s3.bucket}") String bucketName, AmazonS3 amazonS3) {
		this.bucketName = bucketName;
		this.amazonS3 = amazonS3;
	}

	@Override
	public String upload(ImageFileDetail imageFileDetail) {
		String s3Key = imageFileDetail.getUploadFileName(BUCKET_FOLDER);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(imageFileDetail.getContentType());
		objectMetadata.setContentLength(imageFileDetail.getContentLength());

		amazonS3.putObject(bucketName, s3Key, imageFileDetail.getInputStream(), objectMetadata);
		return amazonS3.getUrl(bucketName, s3Key).toString();
	}
}
