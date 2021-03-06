package com.oauth2.server.business.retriever;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;
import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.retriever.handler.AbstractAccessTokenHandler;

public class AuthorizationCodeAccessTokenRetriever extends AbstractAccessTokenHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeAccessTokenRetriever.class);

	private ClientDetails clientDetails;
	private String code;

	public AuthorizationCodeAccessTokenRetriever(ClientDetails clientDetails, String code, UserDetails user) {
		super(user);
		this.clientDetails = clientDetails;
		this.code = code;
	}

	// Always return new AccessToken
	public AccessToken retrieve() throws OAuthSystemException {

		final OauthCode oauthCode = loadOauthCode();
		final String username = oauthCode.username();

		final String clientId = clientDetails.clientId();
		final String authenticationId = iAuthenticationIdGenerator.generate(clientId, username, null);

		AccessToken accessToken = iOAuthCacheRepository.findAccessToken(clientId, username, authenticationId);
		if (accessToken != null) {
			LOG.debug("Delete existed AccessToken: {}", accessToken);
			iOAuthCacheRepository.deleteAccessToken(accessToken);
		}
		accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username,
				authenticationId, user, null);
		LOG.debug("Create a new AccessToken: {}", accessToken);

		return accessToken;
	}

	private OauthCode loadOauthCode() {
		final String clientId = clientDetails.clientId();
		return iOAuthCacheRepository.findOauthCode(code, clientId);
	}
}
