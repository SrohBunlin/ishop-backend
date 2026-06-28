package com.inet.ishop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public void store(MultipartFile file, String filename) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("មិនអាចរក្សាទុកឯកសារបាន!");
        }
    }
}
