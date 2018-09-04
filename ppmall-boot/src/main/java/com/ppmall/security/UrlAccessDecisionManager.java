package com.ppmall.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Spring的决策管理器,有无权限访问的最终觉得权是由投票器来决定的
 * 
 * @author rex
 *
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// TODO Auto-generated method stubIterator<ConfigAttribute> iterator =
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while (iterator.hasNext()) {
			ConfigAttribute ca = iterator.next();
			// 当前请求需要的权限
			String needRole = ca.getAttribute();
			if ("ROLE_LOGIN".equals(needRole)) {
				if (authentication instanceof AnonymousAuthenticationToken) {
					throw new BadCredentialsException("未登录");
				} else
					return;
			}
			// 当前用户所具有的权限
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(needRole)) {
					return;
				}
			}
		}
		throw new AccessDeniedException("权限不足!");

	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

}
