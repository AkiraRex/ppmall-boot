package com.oauth2.server.business.service;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;

public interface IOAuthService {
	ClientDetails loadClientDetails(String clientId);

	String retrieveAuthCode(ClientDetails clientDetails) throws OAuthSystemException; // throws
															// OAuthSystemException;

	AccessToken retrieveAccessToken(ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken) throws OAuthSystemException; // throws
																													// OAuthSystemException;

	// Always return new AccessToken, exclude refreshToken
	AccessToken retrieveNewAccessToken(ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException;// throws
																						// OAuthSystemException;

	OauthCode loadOauthCode(String code, ClientDetails clientDetails);

	boolean removeOauthCode(String code, ClientDetails clientDetails);

	// Always return new AccessToken
	AccessToken retrieveAuthorizationCodeAccessToken(ClientDetails clientDetails, String code) throws OAuthSystemException;// throws
																								// OAuthSystemException;

	// grant_type=password AccessToken
	AccessToken retrievePasswordAccessToken(ClientDetails clientDetails, Set<String> scopes, String username) throws OAuthSystemException;// throws
																												// OAuthSystemException;

	// grant_type=client_credentials
	AccessToken retrieveClientCredentialsAccessToken(ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException;// throws
																										// OAuthSystemException;

	AccessToken loadAccessTokenByRefreshToken(String refreshToken, String clientId);

	AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId) throws OAuthSystemException;// throws
																						// OAuthSystemException;
}
