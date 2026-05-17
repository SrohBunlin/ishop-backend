package com.inet.ishop.config;


import com.inet.ishop.security.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // ១. បង្កើត Object ដោយប្រើ Constructor ទទេ (No-args constructor)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // ២. កំណត់ Service និង Encoder តាមរយៈ Setter methods
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","https://ishop-frontend-production.up.railway.app")); // URL របស់ React
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
                }))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/orders/add").permitAll()
                        .requestMatchers("/api/orders/stats").authenticated()

                        // ២. កំណត់សិទ្ធិដោយប្រើ hasAuthority (កុំប្រើ hasRole)
                        // បើក្នុង DB ប្អូនដាក់ថា "ROLE_ADMIN" នោះក្នុងនេះត្រូវសរសេរ "ROLE_ADMIN" ពេញតែម្តង
                        .requestMatchers(HttpMethod.GET, "/api/orders/all").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        // ៣. ផលិតផលសម្រាប់ Admin
                        .requestMatchers("/api/products/**").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()

                )
                .headers(headers -> headers
                        // 1. ការពារការលួចបង្កប់វេបសាយ (Clickjacking) -> ជំនួស X-Frame-Options
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)

                        // 2. បង្ខំឱ្យប្រើ HTTPS ជានិច្ច -> Strict-Transport-Security (HSTS)
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000) // រយៈពេល ១ ឆ្នាំ
                        )

                        // 3. ការពារកុំឱ្យ Browser ស្មានប្រភេទហ្វាយខុស -> X-Content-Type-Options
                        .contentTypeOptions(contentTypeOptions -> {
                        })

                        // 4. កំណត់ច្បាប់សុវត្ថិភាពមាតិកា -> Content-Security-Policy (CSP)
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;")
                        )

                        // 5. ការពារការលេចធ្លាយព័ត៌មានប្រភពលីង -> Referrer-Policy
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE)
                        ).permissionsPolicyHeader(permissions -> permissions
                                .policy("geolocation=(), microphone=(), camera=()")
                        )
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // បន្ថែមការកំណត់ Headers នៅទីនេះ៖


        return http.build();
    }
}
