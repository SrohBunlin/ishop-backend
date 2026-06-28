package com.inet.ishop.repository;


import com.inet.ishop.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // មិនចាំបាច់សរសេរអ្វីទេ JPA នឹងបង្កើត Method ដូចជា findById() ឱ្យស្រាប់
}
