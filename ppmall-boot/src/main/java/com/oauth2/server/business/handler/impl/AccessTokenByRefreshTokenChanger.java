package com.oauth2.server.business.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.retriever.handler.AbstractAccessTokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class AccessTokenByRefreshTokenChanger extends AbstractAccessTokenHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AccessTokenByRefreshTokenChanger.class);

	private final String refreshToken;
	private final String clientId;

	public AccessTokenByRefreshTokenChanger(String refreshToken, String clientId, UserDetails user) {
		super(user);
		this.refreshToken = refreshToken;
		this.clientId = clientId;
	}

	/**
	 * Get AccessToken Generate a new AccessToken from existed(exclude
	 * token,refresh_token) Update access_token,refresh_token, expired. Save and
	 * remove old
	 */
	public AccessToken change() throws OAuthSystemException {

		final AccessToken oldToken = iOAuthCacheRepository.findAccessTokenByRefreshToken(refreshToken, clientId);

		AccessToken newAccessToken = oldToken.cloneMe();
		LOG.debug("Create new AccessToken: {} from old AccessToken: {}", newAccessToken, oldToken);

		ClientDetails details = iOAuthCacheRepository.findClientDetails(clientId);
		newAccessToken.updateByClientDetails(details);
		final String authId = iAuthenticationIdGenerator.generate(clientId, oldToken.username(), oldToken.scope());
		newAccessToken.authenticationId(authId).tokenId(oAuthIssuer.accessToken())
				.refreshToken(oAuthIssuer.refreshToken());

		iOAuthCacheRepository.deleteAccessToken(oldToken);
		LOG.debug("Delete old AccessToken: {}", oldToken);

		iOAuthCacheRepository.saveAccessToken(newAccessToken);
		LOG.debug("Save new AccessToken: {}", newAccessToken);

		return newAccessToken;
	}
}
