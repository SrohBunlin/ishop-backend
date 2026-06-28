package com.inet.ishop.controller;

import com.inet.ishop.entity.UserProfile;
import com.inet.ishop.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @GetMapping("/{id}")
    public UserProfile getProfile(@PathVariable Long id) {
        return service.getProfile(id);
    }
}