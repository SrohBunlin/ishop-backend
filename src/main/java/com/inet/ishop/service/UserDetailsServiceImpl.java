package com.inet.ishop.service;

import com.inet.ishop.entity.User;
import com.inet.ishop.repository.UserRepository;
import com.inet.ishop.security.UserDetailsImpl;
import jakarta.websocket.Extension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // ✅ Constructor Injection (Best Practice)
    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Extension.Parameter principal = null;
        User user = UserRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(STR."រកមិនឃើញអ្នកប្រើប្រាស់: \{username}"));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                authorities);
    }
}