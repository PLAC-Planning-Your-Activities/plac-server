package com.plac.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.plac.domain.s3.dto.response.UploadImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public UploadImageResponse uploadImage(MultipartFile image, String path) throws IOException {
        String fileName = this.getFileName(image, path);
        ObjectMetadata objectMetadata = this.getObjectMetadata(image);

        amazonS3Client.putObject(bucket, fileName, image.getInputStream(), objectMetadata);

        return new UploadImageResponse(fileName);
    }

    private String getFileName(MultipartFile image, String imagePath) {
        String originalFileName = image.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return imagePath + "/" + UUID.randomUUID() + extension;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return objectMetadata;
    }
}
