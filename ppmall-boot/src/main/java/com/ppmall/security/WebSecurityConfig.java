package com.ppmall.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.DigestUtils;

import com.ppmall.service.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserServiceImpl iUserService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(iUserService).passwordEncoder(new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
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
				String password = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()).toUpperCase();
				return true;
			}
		});
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().and()
				.formLogin().loginPage("/error/not_login.do").loginProcessingUrl("/user/login_p.do")
				.usernameParameter("username").passwordParameter("password").permitAll()
				.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, AuthenticationException e)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						StringBuffer sb = new StringBuffer();
						sb.append("{\"status\":\"error\",\"msg\":\"");

						sb.append("用户名或密码输入错误，登录失败!");

						sb.append("\"}");
						out.write(sb.toString());
						out.flush();
						out.close();
					}
				}).successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, Authentication authentication)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();

						String s = "{\"status\":\"success\",\"msg\":\"成功\"}";
						out.write(s);
						out.flush();
						out.close();
					}
				}).and().logout().permitAll().and().csrf().disable().exceptionHandling();
				
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/reg");
	}
}
