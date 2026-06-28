package com.inet.ishop.entity;

import jakarta.persistence.*; // ប្រើ jakarta.persistence សម្រាប់ Spring Boot 3+
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Data // ជំនួសឱ្យការសរសេរ Getter/Setter ខ្លួនឯង
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    // នេះគឺជាកន្លែងដែលប្អូនរក្សាទុក URL របស់រូបភាព
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

    // ... បន្ថែម Getters/Setters របស់អ្នកនៅទីនេះ
}