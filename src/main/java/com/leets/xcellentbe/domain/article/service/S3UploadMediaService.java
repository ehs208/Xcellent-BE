package com.leets.xcellentbe.domain.article.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.leets.xcellentbe.domain.user.exception.InvalidFileFormat;
import com.leets.xcellentbe.global.error.exception.custom.InternalServerErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadMediaService {
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile multipartFile, String dirName) {
		String fileName = multipartFile.getOriginalFilename();

		if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg"))) {
			throw new InvalidFileFormat();
		}
		try {
			File uploadFile = convert(multipartFile)
				.orElseThrow(()->new RuntimeException());
			return upload(uploadFile, dirName);
		} catch (IOException e) {
			throw new InternalServerErrorException();
		}
	}

	private String upload(File uploadFile, String dirName) {
		String filePath = dirName + "/" + uploadFile.getName();
		String uploadImageUrl = putS3(uploadFile, filePath);

		removeNewFile(uploadFile);

		return uploadImageUrl;
	}

	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
			.withCannedAcl(CannedAccessControlList.PublicRead));

		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일이 삭제되었습니다.");
		} else {
			log.info("파일이 삭제되지 못했습니다.");
		}
	}

	private Optional<File> convert(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			}
			return Optional.of(convertFile);
		}
		return Optional.empty();
	}

	public void removeFile(String fileUrl, String filePath) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		amazonS3Client.deleteObject(bucket, filePath + fileName);
	}
}
