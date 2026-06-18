package com.inet.ishop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 🟢 ១. សម្រាប់ដោះស្រាយបញ្ហា "រូបភាពមិនចេញ" ក្នុង Container (កូដចាស់របស់អ្នក)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/app/images/");
    }

    // 🟢 ២. បន្ថែមផ្នែកនេះចូល ដើម្បីដោះស្រាយបញ្ហា "CORS error" (ធ្វើឱ្យបន្ថែមទំនិញបានជោគជ័យ)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://i-knet.com") // អនុញ្ញាតទាំងពេល Test និងពេល Live
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}