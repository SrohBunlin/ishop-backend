package com.inet.ishop.controller;

import com.inet.ishop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/users") // កំណត់ Path គោលត្រឹមត្រូវ
public class UserController {

    @Autowired
    private FileStorageService fileStorageService;
    // នេះជាកន្លែងដែលប្អូនហៅពី Frontend: /users/upload-profile/{id}
    @PostMapping("/upload-profile/{id}")
    public ResponseEntity<String> uploadProfileImage(@PathVariable String id,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            // ដាក់ឈ្មោះឯកសារឱ្យមានលក្ខណៈពិសេស (ឧទាហរណ៍: profile_3.jpg)
            String fileName = "profile_" + id + ".jpg";

            // ហៅ Service ដើម្បីរក្សាទុក
            fileStorageService.store(file, fileName);

            return ResponseEntity.ok("Upload រូបភាពជោគជ័យ!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("បរាជ័យ: " + e.getMessage());
        }
    }

}