package com.oauth2.util;

import org.springframework.context.ApplicationContext;

public class BeanUtil {
	private static ApplicationContext applicationContext;

	public static void initialize(ApplicationContext applicationContext) {
		BeanUtil.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId) {
		if (applicationContext == null) {
			return null;
		}
		return (T) applicationContext.getBean(beanId);
	}
}
