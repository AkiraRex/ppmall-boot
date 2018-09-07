package com.ppmall.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置静态资源
 * 
 * @author rex
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/dist");
		registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
}
