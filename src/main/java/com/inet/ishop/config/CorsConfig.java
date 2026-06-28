package com.inet.ishop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // ប្តូរពី "*" មកដាក់ Domain របស់ប្អូនផ្ទាល់
                .allowedOrigins("https://i-knet.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // នៅតែទុក true ព្រោះប្អូនត្រូវការផ្ញើ Authorization Header
    }
}
