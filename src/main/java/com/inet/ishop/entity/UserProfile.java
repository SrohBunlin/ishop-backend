package com.inet.ishop.entity;

import jakarta.persistence.*; // ប្រើ jakarta.persistence សម្រាប់ Spring Boot 3+
import java.util.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    private String bio;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String gender;

    @Column(name = "theme_preference")
    private String themePreference;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors, Getters, និង Setters (ដូចដែលអ្នកបានសរសេររួច)
    public UserProfile() {}
    // ... បន្ថែម Getters/Setters របស់អ្នកនៅទីនេះ
}