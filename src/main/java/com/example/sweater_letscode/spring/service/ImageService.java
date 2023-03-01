package com.example.sweater_letscode.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@PropertySource("classpath:spring.properties")
public class ImageService {
    @Value("${image.avatar.bucket.dir}")
    String avatarFilePath;
    @Value("${image.message.bucket.dir}")
    String messagePictureFilePath;

    public void uploadAvatar(String fileName, InputStream imageData) throws IOException {
        var fullPath = Paths.get(avatarFilePath, fileName);
        try (imageData) {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, imageData.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    public Optional<byte[]> loadAvatar(String fileName) throws IOException {
        var fullPath = Paths.get(avatarFilePath, fileName);
        return Files.exists(fullPath)
                ? Optional.of(Files.readAllBytes(fullPath))
                : Optional.empty();
    }

    public void uploadMessagePicture(String fileName, InputStream imageData) throws IOException {
        var fullPath = Paths.get(messagePictureFilePath, fileName);
        try (imageData) {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, imageData.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    public Optional<byte[]> loadMessagePicture(String fileName) throws IOException {
        var fullPath = Paths.get(messagePictureFilePath, fileName);
        return Files.exists(fullPath)
                ? Optional.of(Files.readAllBytes(fullPath))
                : Optional.empty();
    }
}
