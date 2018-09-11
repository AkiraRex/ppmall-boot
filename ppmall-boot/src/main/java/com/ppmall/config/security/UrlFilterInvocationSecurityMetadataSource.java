package com.ppmall.config.security;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

//@Component
@Deprecated
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

	private static Logger logger = LoggerFactory.getLogger(UrlFilterInvocationSecurityMetadataSource.class);
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		logger.info("访问:" + requestUrl);
		
		if (requestUrl.contains("/manage/")) {
        	return SecurityConfig.createList("ROLE_ADMIN");
		}
		
        if (requestUrl.contains("login")||requestUrl.contains("register.do")
        		|| requestUrl.contains("alipay_callback.do")
        		|| requestUrl.contains("check_valid.do")
        		|| requestUrl.contains("product/list.do")
        		|| requestUrl.contains("product/detail.do")
        		|| requestUrl.contains("category/")
        		|| requestUrl.contains("auth")
        		|| requestUrl.contains("error")) {
            return null;
        }
        if (requestUrl.equals("")) {
			
		}
        
        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_USER");
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		 return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
