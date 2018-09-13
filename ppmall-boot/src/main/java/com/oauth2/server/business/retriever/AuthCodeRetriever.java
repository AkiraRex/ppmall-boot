package com.oauth2.server.business.retriever;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;
import com.oauth2.server.business.retriever.holder.AbstractOAuthHolder;

public class AuthCodeRetriever extends AbstractOAuthHolder {
	private static final Logger LOG = LoggerFactory.getLogger(AuthCodeRetriever.class);

	private ClientDetails clientDetails;

	public AuthCodeRetriever(ClientDetails clientDetails) {
		this.clientDetails = clientDetails;
	}

	public String retrieve() throws OAuthSystemException {

		final String clientId = clientDetails.clientId();
		final String username = currentUsername();

		OauthCode oauthCode = iOAuthCacheRepository.findOauthCodeByUsernameClientId(username, clientId);
		if (oauthCode != null) {
			// Always delete exist
			LOG.debug("OauthCode ({}) is existed, remove it and create a new one", oauthCode);
			iOAuthCacheRepository.deleteOauthCode(oauthCode);
		}
		// create a new one
		oauthCode = createOauthCode();

		return oauthCode.code();
	}

	private OauthCode createOauthCode() throws OAuthSystemException {
		final String authCode = oAuthIssuer.authorizationCode();

		LOG.debug("Save OauthCode authorizationCode '{}' of ClientDetails '{}'", authCode, clientDetails);
		final String username = currentUsername();
		OauthCode oauthCode = new OauthCode().code(authCode).username(username).clientId(clientDetails.clientId());

		iOAuthCacheRepository.saveOauthCode(oauthCode);
		return oauthCode;
	}

}
