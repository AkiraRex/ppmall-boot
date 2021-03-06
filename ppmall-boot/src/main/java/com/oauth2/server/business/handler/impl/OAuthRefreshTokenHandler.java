package com.oauth2.server.business.handler.impl;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.server.business.CustomOAuthTokenRequest;
import com.oauth2.server.business.handler.AbstractOAuthTokenHandler;
import com.oauth2.server.business.retriever.RefreshTokenClientDetailsValidator;
import com.oauth2.server.business.validator.AbstractClientDetailsValidator;
import com.oauth2.util.WebUtils;

public class OAuthRefreshTokenHandler extends AbstractOAuthTokenHandler {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthRefreshTokenHandler.class);

	@Override
	public boolean support(CustomOAuthTokenRequest tokenRequest) throws OAuthProblemException {
		final String grantType = tokenRequest.getGrantType();
		return GrantType.REFRESH_TOKEN.toString().equalsIgnoreCase(grantType);
	}

	/**
     * URL demo:
     * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=refresh_token&refresh_token=b36f4978-a172-4aa8-af89-60f58abe3ba1
     *
     * @throws org.apache.oltu.oauth2.common.exception.OAuthProblemException
     */
    @Override
    public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {

        final String refreshToken = tokenRequest.getRefreshToken();
        AccessToken accessToken = iOAuthService.changeAccessTokenByRefreshToken(refreshToken, tokenRequest.getClientId());

        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        LOG.debug("'refresh_token' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse(response, tokenResponse);

    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new RefreshTokenClientDetailsValidator(tokenRequest);
    }




}
