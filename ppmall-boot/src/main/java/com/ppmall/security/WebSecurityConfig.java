//package com.ppmall.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//
////@Configuration
////@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//                   .withUser("yiibai").password("123456").roles("USER");
//	}
//
//	//.csrf() is optional, enabled by default, if using WebSecurityConfigurerAdapter constructor
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		  http
//          .authorizeRequests()
//              .antMatchers("/", "/home").permitAll()
//              .anyRequest().authenticated()
//              .and()
//          .formLogin()
//              .loginPage("/login")
//              .permitAll()
//              .and()
//          .logout()
//              .permitAll();	
//		  
//	}
//}
