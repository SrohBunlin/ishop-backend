package com.inet.ishop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // កំណត់ Secret Key និងពេលវេលាផុតកំណត់
    private final String jwtSecret = "mySecretKeymySecretKeymySecretKeymySecretKey";
    private final long jwtExpirationMs = 86400000; // 1 ថ្ងៃ

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ១. បង្កើត Token និងបញ្ចូល Roles ទៅក្នុង Claims
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles) // ✅ រក្សាទុក Role ក្នុង Token ដើម្បីកុំឱ្យជាប់ 403
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ២. ស្រង់យកឈ្មោះ User ចេញពី Token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // ៣. ✅ បន្ថែមថ្មី៖ ស្រង់យក Roles ចេញពី Token (សំខាន់បំផុតសម្រាប់ដោះស្រាយ 403)
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

    // ៤. ផ្ទៀងផ្ទាត់ភាពត្រឹមត្រូវនៃ Token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage());
        }
        return false;
    }
}