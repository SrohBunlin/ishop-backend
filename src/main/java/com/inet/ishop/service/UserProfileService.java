package com.inet.ishop.service;

import com.inet.ishop.entity.UserProfile;
import com.inet.ishop.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository repository;

    public UserProfile getProfile(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    public UserProfile saveProfile(UserProfile profile) {
        return repository.save(profile);
    }
}