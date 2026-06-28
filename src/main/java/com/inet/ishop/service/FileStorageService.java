package com.inet.ishop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    // ប្រើ Path នេះដែលជា Storage របស់ប្អូន
    private final Path root = Paths.get(System.getProperty("user.home"), "ishop-uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (Exception e) {
            throw new RuntimeException("មិនអាចបង្កើត folder បាន!");
        }
    }
    public void store(MultipartFile file, String filename) {
        try {
            // រក្សាទុកឯកសារចូលទៅក្នុង path ដែលបានកំណត់
            Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("មិនអាចរក្សាទុកឯកសារបាន: " + e.getMessage());
        }
    }
}
