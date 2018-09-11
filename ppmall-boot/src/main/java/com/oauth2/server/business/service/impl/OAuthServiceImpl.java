package com.oauth2.server.business.service.impl;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;
import com.oauth2.repository.IOAuthCacheRepository;
import com.oauth2.server.business.retriever.AccessTokenRetriever;
import com.oauth2.server.business.retriever.AuthCodeRetriever;
import com.oauth2.server.business.retriever.AuthorizationCodeAccessTokenRetriever;
import com.oauth2.server.business.retriever.NewAccessTokenRetriever;
import com.oauth2.server.business.retriever.PasswordAccessTokenRetriever;
import com.oauth2.server.business.service.IOAuthService;

@Service("iOAuthService")
public class OAuthServiceImpl implements IOAuthService {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthServiceImpl.class);

	@Autowired
	private IOAuthCacheRepository iOAuthCacheRepository;

	/**
	 * Load ClientDetails instance by clientId
	 *
	 * @param clientId
	 *            clientId
	 * @return ClientDetails
	 */
	@Override
	public ClientDetails loadClientDetails(String clientId) {
		LOG.debug("Load ClientDetails by clientId: {}", clientId);
		return iOAuthCacheRepository.findClientDetails(clientId);
	}

	/**
	 * Retrieve an existed code, if it is existed , remove it and create a new
	 * one, otherwise, create a new one and return
	 *
	 * @param clientDetails
	 *            ClientDetails
	 * @return code
	 * @throws OAuthSystemException
	 */
	@Override
	public String retrieveAuthCode(ClientDetails clientDetails) throws OAuthSystemException {
		AuthCodeRetriever authCodeRetriever = new AuthCodeRetriever(clientDetails);
		return authCodeRetriever.retrieve();
	}

	@Override
	public AccessToken retrieveAccessToken(ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken)
			throws OAuthSystemException {
		AccessTokenRetriever retriever = new AccessTokenRetriever(clientDetails, scopes, includeRefreshToken);
		return retriever.retrieve();
	}

	// Always return new AccessToken, exclude refreshToken
	@Override
	public AccessToken retrieveNewAccessToken(ClientDetails clientDetails, Set<String> scopes)
			throws OAuthSystemException {
		NewAccessTokenRetriever tokenRetriever = new NewAccessTokenRetriever(clientDetails, scopes);
		return tokenRetriever.retrieve();
	}

	@Override
	public OauthCode loadOauthCode(String code, ClientDetails clientDetails) {
		final String clientId = clientDetails.clientId();
		return iOAuthCacheRepository.findOauthCode(code, clientId);
	}

	@Override
	public boolean removeOauthCode(String code, ClientDetails clientDetails) {
		final OauthCode oauthCode = loadOauthCode(code, clientDetails);
		final int rows = iOAuthCacheRepository.deleteOauthCode(oauthCode);
		return rows > 0;
	}

	// Always return new AccessToken
	@Override
	public AccessToken retrieveAuthorizationCodeAccessToken(ClientDetails clientDetails, String code)
			throws OAuthSystemException {
		AuthorizationCodeAccessTokenRetriever codeAccessTokenRetriever = new AuthorizationCodeAccessTokenRetriever(
				clientDetails, code);
		return codeAccessTokenRetriever.retrieve();
	}

	// grant_type=password AccessToken
	@Override
	public AccessToken retrievePasswordAccessToken(ClientDetails clientDetails, Set<String> scopes, String username)
			throws OAuthSystemException {
		PasswordAccessTokenRetriever tokenRetriever = new PasswordAccessTokenRetriever(clientDetails, scopes, username);
		return tokenRetriever.retrieve();
	}

	/*
	 * Get AccessToken Generate a new AccessToken from existed(exclude
	 * token,refresh_token) Update access_token,refresh_token, expired. Save and
	 * remove old
	 */
	@Override
	public AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId)
			throws OAuthSystemException {
		// AccessTokenByRefreshTokenChanger refreshTokenChanger = new
		// AccessTokenByRefreshTokenChanger(refreshToken, clientId);
		// return refreshTokenChanger.change();
		return null;
	}

	// grant_type=client_credentials
	@Override
	public AccessToken retrieveClientCredentialsAccessToken(ClientDetails clientDetails, Set<String> scopes)
			throws OAuthSystemException {
		// ClientCredentialsAccessTokenRetriever tokenRetriever = new
		// ClientCredentialsAccessTokenRetriever(clientDetails, scopes);
		// return tokenRetriever.retrieve();
		return null;
	}

	@Override
	public AccessToken loadAccessTokenByRefreshToken(String refreshToken, String clientId) {
		LOG.debug("Load ClientDetails by refreshToken: {} and clientId: {}", refreshToken, clientId);
		return iOAuthCacheRepository.findAccessTokenByRefreshToken(refreshToken, clientId);
	}

}
