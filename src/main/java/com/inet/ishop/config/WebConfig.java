package com.inet.ishop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // នៅពេល Frontend ហៅមកលីង https://api.i-knet.com/images/iphone16.png
        // វានឹងទៅទាញយកឯកសារពី Folder /app/images/ នៅក្នុង Container មកឱ្យភ្លាម
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/app/images/");
    }
}
