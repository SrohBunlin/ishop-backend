package com.inet.ishop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    // កំណត់ path ក្នុង container ឱ្យច្បាស់លាស់
    private final Path root = Paths.get("/app/uploads");

    public FileStorageService() {
        try {
            // បង្កើត directory ប្រសិនបើវាមិនទាន់មាន
            Files.createDirectories(root);
        } catch (Exception e) {
            throw new RuntimeException("មិនអាចបង្កើត Folder ផ្ទុកឯកសារបាន!");
        }
    }

    public void store(MultipartFile file, String filename) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (Exception e) {
            e.printStackTrace(); // បន្ថែមដើម្បីងាយស្រួល Debug
            throw new RuntimeException("មិនអាចរក្សាទុកឯកសារបាន: " + e.getMessage());
        }
    }
}
