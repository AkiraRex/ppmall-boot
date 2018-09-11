package com.oauth2.repository.impl;

import org.springframework.stereotype.Component;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;
import com.oauth2.repository.IOAuthRepository;

@Component("iOAuthRepository")
public class OAuthRepositoryImpl implements IOAuthRepository {

	@Override
	public ClientDetails findClientDetails(String clientId) {
		// TODO Auto-generated method stub
		ClientDetails clientDetails = new ClientDetails();
		clientDetails
			.name("test")
			.clientSecret("test")
			.grantTypes("password");
		return clientDetails;
	}

	@Override
	public int saveClientDetails(ClientDetails clientDetails) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveOauthCode(OauthCode oauthCode) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OauthCode findOauthCode(String code, String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OauthCode findOauthCodeByUsernameClientId(String username, String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteOauthCode(OauthCode oauthCode) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AccessToken findAccessToken(String clientId, String username, String authenticationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteAccessToken(AccessToken accessToken) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveAccessToken(AccessToken accessToken) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AccessToken findAccessTokenByRefreshToken(String refreshToken, String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

}
