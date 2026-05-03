package com.inet.ishop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255) // ប្រវែង ២៥៥ សំខាន់ណាស់សម្រាប់ BCrypt
    private String password;

    @Column(length = 50)
    private String role; // ឧទាហរណ៍៖ "ROLE_ADMIN"

    private boolean enabled = true; // បន្ថែមនេះដើម្បីឱ្យស៊ីជាមួយ SQL 'enabled'
}