package com.ppmall.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Scope
public class GlobalAspest {

	@Pointcut("@annotation(com.ppmall.aop.annotation.AccessConclusion)")  
	public void logForIndex() {

	}

	@Around("logForIndex()")
	public Object around(ProceedingJoinPoint joinPoint) {
		System.out.println("Index!");
		Object object = null;
		try {
			object = joinPoint.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
}
