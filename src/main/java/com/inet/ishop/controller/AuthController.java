package com.inet.ishop.controller;

import com.inet.ishop.dto.JwtResponse;
import com.inet.ishop.dto.LoginRequest;
import com.inet.ishop.security.JwtUtils;
import com.inet.ishop.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // ១. ផ្ទៀងផ្ទាត់សិទ្ធិ (Authentication)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ២. បង្កើត Token (ត្រូវបញ្ជូន object authentication ទៅឱ្យវា)
        String jwt = jwtUtils.generateJwtToken(authentication);

        // ៣. ទាញយក UserDetailsImpl (Cast ពី Principal)
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // ៤. ✅ ប្រកាស Variable 'roles' (បន្ទាត់នេះហើយដែលបាត់ ទើបវា Error)
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // ៥. បញ្ជូនលទ្ធផលទៅឱ្យ Client (ប្រើ variable roles ដែលទើបបង្កើត)
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles)); // ឥឡូវវានឹងស្គាល់ variable roles នេះហើយ
    }
}