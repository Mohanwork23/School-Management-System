package com.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements com.service.FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String saveFile(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            log.info("File saved at: {}", filePath.toAbsolutePath());

            return baseUrl + "/files/" + fileName;
        } catch (IOException e) {
            log.error("File save failed for: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("File save failed: " + e.getMessage(), e);
        }
    }
}
