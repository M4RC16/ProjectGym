package com.projectgym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${file.upload-dir-user}")
    private String uploadDir;

    @Value("${file.upload-dir-gallery}")
    private String uploadDirGallery;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:4200",
                                "https://projectgym.hu",
                                "https://www.projectgym.hu"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/images/users/**")
                        .addResourceLocations("file:" + uploadDir + "/");

                registry.addResourceHandler("/images/gallery/**")
                        .addResourceLocations("file:" + uploadDirGallery + "/");
            }
        };
    }
}