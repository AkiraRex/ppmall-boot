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
        if (requestUrl.contains("login")) {
            return null;
        }
        if (requestUrl.contains("/manage/")) {
        	return SecurityConfig.createList("ROLE_超级管理员");
		}
//        List<Menu> allMenu = menuService.getAllMenu();
//        for (Menu menu : allMenu) {
//            if (antPathMatcher.match(menu.getUrl(), requestUrl)&&menu.getRoles().size()>0) {
//                List<Role> roles = menu.getRoles();
//                int size = roles.size();
//                String[] values = new String[size];
//                for (int i = 0; i < size; i++) {
//                    values[i] = roles.get(i).getName();
//                }
//                return SecurityConfig.createList(values);
//            }
//        }
        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_普通用户");
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

}
