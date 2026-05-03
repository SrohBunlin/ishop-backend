package com.inet.ishop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    // Getter និង Setter (ឬប្រើ @Data បើអ្នកប្រើ Lombok)
    private String token;
    private Long id;
    private String username;
    private List<String> roles;

    // ✅ ត្រូវតែមាន Constructor នេះដើម្បីបំបាត់ Error ក្នុង AuthController
    public JwtResponse(String accessToken, Long id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

}