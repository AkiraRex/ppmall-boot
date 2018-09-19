package com.oauth2.server.business.service;

import com.oauth2.domain.UserDetails;

public interface IUserDetailsService {
	UserDetails loadUserByUsername(String username);
}
