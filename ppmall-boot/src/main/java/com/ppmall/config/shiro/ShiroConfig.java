package com.ppmall.config.shiro;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.apache.shiro.realm.Realm;

import com.oauth2.common.OAuth2CredentialsMatcher;
import com.oauth2.factory.OAuth2SubjectFactory;
import com.oauth2.resource.filter.OAuth2Filter;
import com.ppmall.config.shiro.realm.BasicShiroRealm;
import com.ppmall.config.shiro.realm.CustomShiroRealm;
import com.ppmall.config.shiro.realm.RSRedisRealm;
@Configuration
public class ShiroConfig {

	@Autowired
	OAuth2Filter oAuth2Filter;

	/**
	 * 自定义的Realm
	 */
	@Bean
	public CustomShiroRealm customShiroRealm() {
		CustomShiroRealm customShiroRealm = new CustomShiroRealm();
		customShiroRealm.setCredentialsMatcher(credentialsMatcher());
		return customShiroRealm;
	}
	
	@Bean
	public CredentialsMatcher authzCredentialsMatcher(){
		return new HashedCredentialsMatcher("MD5");

	}
	
	@Bean
	public CredentialsMatcher resourcesCredentialsMatcher(){
		return new SimpleCredentialsMatcher();
	}
	
	@Bean
	public CredentialsMatcher credentialsMatcher() {
		OAuth2CredentialsMatcher oAuth2CredentialsMatcher = new OAuth2CredentialsMatcher();
		oAuth2CredentialsMatcher.setAuthzCredentialsMatcher(authzCredentialsMatcher());
		oAuth2CredentialsMatcher.setResourcesCredentialsMatcher(resourcesCredentialsMatcher());
		return oAuth2CredentialsMatcher;
	}
	
	@Bean
	public BasicShiroRealm basicShiroRealm(){
		BasicShiroRealm basicShiroRealm = new BasicShiroRealm();
		basicShiroRealm.setCredentialsMatcher(credentialsMatcher());
		return basicShiroRealm;
	}
	
	@Bean
	public RSRedisRealm rSRedisRealm() {
		RSRedisRealm rsRedisRealm = new RSRedisRealm();
		rsRedisRealm.setCredentialsMatcher(credentialsMatcher());
		return rsRedisRealm;
	}

	@Bean
	public OAuth2SubjectFactory subjectFactory() {
		return new OAuth2SubjectFactory();
	}

	@Bean
	public CacheManager shiroCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		List<Realm> realms = new ArrayList<>();
		realms.add(rSRedisRealm());
		realms.add(basicShiroRealm());
		securityManager.setRealms(realms);
		securityManager.setSubjectFactory(subjectFactory());
		securityManager.setCacheManager(shiroCacheManager());
		return securityManager;
	}

	@Bean
	public OAuthIssuer oAuthIssuer() {
		return new OAuthIssuerImpl(new MD5Generator());
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/error/notLogin");

		shiroFilterFactoryBean.setUnauthorizedUrl("/error/notAuth");

		// OAuth2过滤器.
		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("oauth", oAuth2Filter);
		shiroFilterFactoryBean.setFilters(filterMap);

		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/cart/**", "oauth");
		// 配置不会被拦截的链接 顺序判断

		filterChainDefinitionMap.put("/user/login", "anon");

		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");

		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 :这是一个坑呢，一不小心代码就不好使了;
		// ① authc:所有url都必须认证通过才可以访问; ② anon:所有url都都可以匿名访问
		filterChainDefinitionMap.put("/**", "anon");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
	 * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)
	 * 和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 * 
	 * @return
	 */
	@Bean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 異常處理
	 * 
	 * @return
	 */
//	@Bean
//	public HandlerExceptionResolver solver() {
//		HandlerExceptionResolver handlerExceptionResolver = new CustomShiroExceptionResolver();
//		return handlerExceptionResolver;
//	}
	
	/**
	 * filter
	 */
	@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> testFilterRegistration() {

		FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<DelegatingFilterProxy>();
		registration.setFilter(new DelegatingFilterProxy());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("targetFilterLifecycle", "true");
		registration.setName("shiroFilter");
		registration.setOrder(1);
		return registration;
	}
}
