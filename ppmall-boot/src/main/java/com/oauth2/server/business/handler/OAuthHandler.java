package com.oauth2.server.business.handler;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.server.business.service.IOAuthService;
import com.oauth2.util.BeanUtil;

public abstract class OAuthHandler {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthHandler.class);

	protected IOAuthService iOAuthService = BeanUtil.getBean(IOAuthService.class);

	private ClientDetails clientDetails;

	protected ClientDetails clientDetails() {
		if (clientDetails == null) {
			final String clientId = clientId();
			clientDetails = iOAuthService.loadClientDetails(clientId);
			LOG.debug("Load ClientDetails: {} by clientId: {}", clientDetails, clientId);
		}
		return clientDetails;
	}

	/**
	 * Create AccessToken response
	 *
	 * @param accessToken
	 *            AccessToken
	 * @param queryOrJson
	 *            True is QueryMessage, false is JSON message
	 * @return OAuthResponse
	 * @throws org.apache.oltu.oauth2.common.exception.OAuthSystemException
	 */
	protected OAuthResponse createTokenResponse(AccessToken accessToken, boolean queryOrJson)
			throws OAuthSystemException {
		final ClientDetails clientDetails = clientDetails();

		final OAuthASResponse.OAuthTokenResponseBuilder builder = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK).location(clientDetails.redirectUri())
				.setAccessToken(accessToken.tokenId())
				.setExpiresIn(String.valueOf(accessToken.currentTokenExpiredSeconds()))
				.setTokenType(accessToken.tokenType());

		final String refreshToken = accessToken.refreshToken();
		if (StringUtils.isNotEmpty(refreshToken)) {
			builder.setRefreshToken(refreshToken);
		}

		return queryOrJson ? builder.buildQueryMessage() : builder.buildJSONMessage();
	}

	protected abstract String clientId();
}
