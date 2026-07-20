package com.service.implement;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements com.service.FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public String saveFile(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            System.out.println("✅ File saved at: " + filePath.toAbsolutePath());

            return "http://localhost:8080/files/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ File save failed: " + e.getMessage(), e);
        }
    }
}
