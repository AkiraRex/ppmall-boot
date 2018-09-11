package com.oauth2.server.business.retriever;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.server.business.retriever.handler.AbstractAccessTokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

public class NewAccessTokenRetriever extends AbstractAccessTokenHandler {

	private static final Logger LOG = LoggerFactory.getLogger(NewAccessTokenRetriever.class);

	private final ClientDetails clientDetails;
	private final Set<String> scopes;

	public NewAccessTokenRetriever(ClientDetails clientDetails, Set<String> scopes) {
		this.clientDetails = clientDetails;
		this.scopes = scopes;
	}

	// Always return new AccessToken, exclude refreshToken
	public AccessToken retrieve() throws OAuthSystemException {

		String scopeAsText = getScope();
		final String username = currentUsername();
		final String clientId = clientDetails.clientId();

		final String authenticationId = iAuthenticationIdGenerator.generate(clientId, username, scopeAsText);

		AccessToken accessToken = iOAuthRepository.findAccessToken(clientId, username, authenticationId);
		if (accessToken != null) {
			LOG.debug("Delete existed AccessToken: {}", accessToken);
			iOAuthRepository.deleteAccessToken(accessToken);
		}
		accessToken = createAndSaveAccessToken(clientDetails, false, username, authenticationId);
		LOG.debug("Create a new AccessToken: {}", accessToken);

		return accessToken;
	}

	private String getScope() {
		if (scopes != null) {
			return OAuthUtils.encodeScopes(scopes);
		} else {
			return null;
		}
	}
}