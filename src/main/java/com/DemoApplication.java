package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Configuration
	public class CorsConfig implements WebMvcConfigurer {
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
			// 域名根目录访问微信域名配置校验文件
			registry.addResourceHandler("/**").addResourceLocations("classpath:/wx/");

		}
	}

}
