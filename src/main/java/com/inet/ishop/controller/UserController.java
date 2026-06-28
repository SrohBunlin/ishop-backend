package com.inet.ishop.controller;

import com.inet.ishop.dto.UserDTO;
import com.inet.ishop.entity.User;
import com.inet.ishop.entity.UserProfile;
import com.inet.ishop.repository.UserProfileRepository;
import com.inet.ishop.repository.UserRepository;
import com.inet.ishop.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://i-knet.com")
public class UserController {
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository; // ប្អូនត្រូវការវាដើម្បី save profile

    public UserController(FileStorageService fileStorageService, UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @PutMapping("/users") // កុំសរសេរ /api/api/user
    public ResponseEntity<?> updateProfile(
            Principal principal,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        try {
            // ១. ស្វែងរក User
            User user = UserRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("រកមិនឃើញអ្នកប្រើប្រាស់"));

            // ២. Update ឈ្មោះ
            user.setFirstName(firstName);
            user.setLastName(lastName);

            // ៣. គ្រប់គ្រង Profile
            UserProfile profile = user.getUserProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUserId(user.getId());
            }

            if (avatar != null && !avatar.isEmpty()) {
                String fileName = STR."\{UUID.randomUUID().toString()}_\{avatar.getOriginalFilename()}";
                fileStorageService.store(avatar, fileName);
                profile.setProfilePictureUrl(STR."/uploads/\{fileName}");
                userProfileRepository.save(profile);
            }

            userRepository.save(user);

            UserDTO updatedUserDTO = new UserDTO(user.getFirstName(), user.getLastName(), profile.getProfilePictureUrl());
            return ResponseEntity.ok(updatedUserDTO);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(STR."បរាជ័យក្នុងការ Update Profile: \{e.getMessage()}");
        }
    }
}