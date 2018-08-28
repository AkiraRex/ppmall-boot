package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.ppmall.dao")
@ServletComponentScan
public class PPMALLApplication {
	public static void main(String[] args) {
		SpringApplication.run(PPMALLApplication.class, args);
	}
}
