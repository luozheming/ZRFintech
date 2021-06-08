package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 用profile区分不同环境的静态文件
        String basePath = "classpath:static/" + profile;
        // 活动h5页面
        registry.addResourceHandler("/activity/**")
                .addResourceLocations(basePath + "/activity/dist/");
        // web版后台管理登录页面
        registry.addResourceHandler("/management/**")
                .addResourceLocations(basePath + "/management/dist/");
        // 小程序嵌套h5页面
        registry.addResourceHandler("/applet/**")
                .addResourceLocations(basePath + "/applet/dist/");
        // 小程序模拟路演提示音频路径
        registry.addResourceHandler("/applet/voicePrompt/**")
                .addResourceLocations(basePath + "/applet/voicePrompt/");
        // pc版登录界面
        registry.addResourceHandler("/pc/**")
                .addResourceLocations(basePath + "/pc/dist/");
        // 根目录访问页面
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:static/","classpath:static/wx/");
    }


}
