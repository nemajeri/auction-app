package com.atlantbh.auctionappbackend.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.atlantbh.auctionappbackend.exception.FileDeletionException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String s3KeyPrefix) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = s3KeyPrefix + UUID.randomUUID() + "_" + file.getOriginalFilename();
        s3client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        try {
            Files.delete(fileObj.toPath());
        } catch (IOException e) {
            throw new FileDeletionException("Could not delete file: " + fileName);
        }
        return s3client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
