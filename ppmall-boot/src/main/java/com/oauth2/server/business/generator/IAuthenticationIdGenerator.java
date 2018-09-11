package com.oauth2.server.business.generator;

public interface IAuthenticationIdGenerator {
	public String generate(String clientId, String username, String scope);
}
