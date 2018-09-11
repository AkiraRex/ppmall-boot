package com.ppmall.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import com.ppmall.pojo.User;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.service.impl.UserServiceImpl;
import com.ppmall.util.MD5Util;

//@Configuration
@Deprecated
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserServiceImpl iUserService;
	
	@Autowired
	private UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
	
	@Autowired
	private UrlAccessDecisionManager urlAccessDecisionManager;
	
	@Autowired
	private AuthenticationAccessDeniedHandler accessDeniedHandler;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
    PasswordEncoder passwordEncoder(){
        return new PasswordEncoder() {
			
			@Override
			public String encode(CharSequence charSequence) {
				return MD5Util.MD5EncodeUtf8(charSequence.toString());
			}

			/**
			 * @param charSequence
			 *            明文
			 * @param s
			 *            密文
			 * @return
			 */
			@Override
			public boolean matches(CharSequence charSequence, String s) {
				String password = MD5Util.MD5EncodeUtf8(charSequence.toString()).toUpperCase();
				return password.equals(s);
			}
		};
    }
    
//    String finalPassword = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");
//    String finalSecret = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//		auth.userDetailsService(iUserService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O o) {
						o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
						o.setAccessDecisionManager(urlAccessDecisionManager);
						return o;
					}
				})
				.and()
				.formLogin().loginPage("/error/not_login.do")
				.loginProcessingUrl("/manage/user/login.do").usernameParameter("username").passwordParameter("password")
				.and().formLogin().loginPage("/error/not_login.do")
				.loginProcessingUrl("/user/login.do").usernameParameter("username").passwordParameter("password")
				.permitAll().failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, AuthenticationException e)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();

						ServerResponse<String> response = ServerResponse.createErrorMessage("登陆失败:" + e.getMessage());
						out.write(response.toString());
						out.flush();
						out.close();
						e.printStackTrace();
					}
				}).successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, Authentication authentication)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						User user = (User) authentication.getPrincipal();
						httpServletRequest.getSession().setAttribute(Const.CURRENT_USER	, user);
						PrintWriter out = httpServletResponse.getWriter();
						
						ServerResponse<User> response = ServerResponse.createSuccess(user);

						out.write(response.toString());
						out.flush();
						out.close();
					}
				}).and().logout().permitAll()
				  .and().csrf().disable().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	

	}

}
