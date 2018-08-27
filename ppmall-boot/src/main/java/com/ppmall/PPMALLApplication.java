package com.ppmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.ppmall.dao")
public class PPMALLApplication {
	public static void main(String[] args) {
		SpringApplication.run(PPMALLApplication.class, args);
	}
}
