package com.tripbros.server.user.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ImageService {
	private final BlobServiceClient blobServiceClient;

	@Value("${azure_container_name}")
	private String containerName;

	public String uploadImageAndGetName(MultipartFile file) {
		try {
			BlobContainerClient client = blobServiceClient.getBlobContainerClient(containerName);
			String fileName = UUID.randomUUID().toString() + "." + getExtension(file.getOriginalFilename()).orElseThrow(() -> new IllegalStateException("확장자 이상 발생"));
			BlobClient blobClient = client.getBlobClient(fileName);
			blobClient.upload(file.getInputStream(), file.getSize());
			return blobClient.getBlobUrl();
		} catch (IOException e) {
			log.info("이미지 업로드 실패");
			throw new IllegalArgumentException("이미지 업로드 실패");
		}
	}

	public String downloadImage(String imageName) {
		BlobContainerClient client = blobServiceClient.getBlobContainerClient(containerName);
		BlobClient blobClient = client.getBlobClient(imageName);
		return blobClient.getBlobUrl();
	}

	public void deleteImage(String imageUrl) {
		BlobContainerClient client = blobServiceClient.getBlobContainerClient(containerName);
		BlobClient blobClient = client.getBlobClient(getFilenameFromUrl(imageUrl));
		blobClient.delete();
	}

	private static Optional<String> getExtension(String filename) {
		log.info("이미지 파일 이름 : {}", filename);
		return Optional.ofNullable(filename)
			.filter(f -> f.contains("."))
			.map(f -> f.substring(f.lastIndexOf(".") + 1));
	}

	private static String getFilenameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

}
