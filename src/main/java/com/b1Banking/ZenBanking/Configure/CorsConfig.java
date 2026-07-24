package com.b1Banking.ZenBanking.Configure;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "https://devbank.web.app",
                    "https://devbank.firebaseapp.com",
                    "https://devbank-frontend.vercel.app"
                )
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}