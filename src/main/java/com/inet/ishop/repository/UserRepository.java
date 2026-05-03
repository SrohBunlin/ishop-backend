package com.inet.ishop.repository;

import com.inet.ishop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ស្វែងរកអ្នកប្រើប្រាស់តាមរយៈ Username (ប្រើសម្រាប់ Authentication)
    Optional<User> findByUsername(String username);

    // ឆែកមើលថាតើ Username មានរួចហើយឬនៅ (ប្រើសម្រាប់ Registration)
    Boolean existsByUsername(String username);
}
