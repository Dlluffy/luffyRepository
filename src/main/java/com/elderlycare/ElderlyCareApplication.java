package com.elderlycare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.elderlycare.mapper")
public class ElderlyCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElderlyCareApplication.class, args);
	}

}
