package com.inet.ishop.controller;

import com.inet.ishop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users") // ១. ប្តូរទៅជា /api/users ឱ្យត្រូវនឹងអ្វីដែលលោតក្នុង Error (path: "/api/users")
public class UserController {

    @Autowired
    private FileStorageService fileStorageService;

    // ២. បន្ថែម GetMapping នេះដើម្បីឱ្យ Frontend អាចទាញយកទិន្នន័យ User បាន (បំបាត់ 404)
    @GetMapping
    public ResponseEntity<String> getUserProfile() {
        // កន្លែងនេះប្អូនអាចកែសម្រួលដើម្បី Return ជា Object ឬទិន្នន័យ User ចេញពី Database នៅពេលក្រោយ
        return ResponseEntity.ok("តេស្ត៖ ភ្ជាប់ទៅកាន់ Endpoint របស់ User ជោគជ័យ!");
    }

    // នេះជាកន្លែងដែលប្អូនហៅពី Frontend សម្រាប់ Upload រូបភាព៖ /api/users/upload-profile/{id}
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