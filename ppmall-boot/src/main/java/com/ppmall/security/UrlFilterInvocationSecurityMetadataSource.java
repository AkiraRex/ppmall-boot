package com.ppmall.security;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if (requestUrl.contains("/manage/")) {
        	return SecurityConfig.createList("ROLE_ADMIN");
		}
		
        if (requestUrl.contains("login")||requestUrl.contains("register.do")
        		|| requestUrl.contains("alipay_callback.do")
        		|| requestUrl.contains("check_valid.do")
        		|| requestUrl.contains("product/list.do")
        		|| requestUrl.contains("product/detail.do")
        		|| requestUrl.contains("category/")) {
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
