package com.inet.ishop.service;

import com.inet.ishop.entity.Product;
import com.inet.ishop.entity.User;
import com.inet.ishop.repository.ProductRepository;
import com.inet.ishop.repository.UserRepository;
import com.inet.ishop.security.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<Product> searchProducts(String query) {
        return productRepository.searchGlobal(query);
    }
    // ✅ Constructor Injection (Best Practice)
    public UserDetailsServiceImpl(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("រកមិនឃើញអ្នកប្រើប្រាស់: " + username));

        // ✅ ដោយសារក្នុង DB មាន "ROLE_ADMIN" រួចហើយ យើងបោះវាចូលត្រង់ៗតែម្ដង
        // មិនចាំបាច់ប្រើ .toUpperCase() ទេ ព្រោះវាអាចធ្វើឱ្យមានបញ្ហាបើក្នុង DB សរសេរអក្សរតូច
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