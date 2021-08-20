package com;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableConfigurationProperties
@EnableAsync
public class DemoApplication {

	@Value("${spring.profiles.active}")
	private String profile;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		ParserConfig.getGlobalInstance().setSafeMode(true);
	}

}
