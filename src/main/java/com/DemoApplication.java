package com;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoApplication {

	@Value("${spring.profiles.active}")
	private String profile;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		ParserConfig.getGlobalInstance().setSafeMode(true);
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
			// 用profile区分不同环境的静态文件
			String basePath = "classpath:static/" + profile;
			// 活动h5页面
			registry.addResourceHandler("/activity/**")
					.addResourceLocations(basePath + "/activity/dist/");
			// web版后台管理登录页面
			registry.addResourceHandler("/web/**")
					.addResourceLocations(basePath + "/web/dist/");
			// 小程序嵌套h5页面
			registry.addResourceHandler("/applet/**")
					.addResourceLocations(basePath + "/applet/dist/");
			// 根目录访问页面
			registry.addResourceHandler("/**")
					.addResourceLocations("classpath:static/","classpath:static/wx/");
		}
	}

}
