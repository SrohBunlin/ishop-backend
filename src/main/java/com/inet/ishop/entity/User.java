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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private UserProfile userProfile;
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
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public void setAvatarUrl(String s) {
    }

    public String getAvatarUrl() {
        return "";
    }
}