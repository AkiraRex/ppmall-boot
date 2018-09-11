package com.oauth2.server.business.handler;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.server.business.CustomOAuthTokenRequest;
import com.oauth2.server.business.validator.AbstractClientDetailsValidator;
import com.oauth2.util.WebUtils;

public abstract class AbstractOAuthTokenHandler extends OAuthHandler implements OAuthTokenHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractOAuthTokenHandler.class);

	protected CustomOAuthTokenRequest tokenRequest;
	protected HttpServletResponse response;

	@Override
	public final void handle(CustomOAuthTokenRequest tokenRequest, HttpServletResponse response)
			throws OAuthProblemException, OAuthSystemException {
		this.tokenRequest = tokenRequest;
		this.response = response;

		// validate
		if (validateFailed()) {
			return;
		}

		handleAfterValidation();
	}

	protected boolean validateFailed() throws OAuthSystemException {
		AbstractClientDetailsValidator validator = getValidator();
		LOG.debug("Use [{}] validate client: {}", validator, tokenRequest.getClientId());

		final OAuthResponse oAuthResponse = validator.validate();
		return checkAndResponseValidateFailed(oAuthResponse);
	}

	protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
		if (oAuthResponse != null) {
			LOG.debug("Validate OAuthAuthzRequest(client_id={}) failed", tokenRequest.getClientId());
			WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
			return true;
		}
		return false;
	}

	protected abstract AbstractClientDetailsValidator getValidator();

	protected String clientId() {
		return tokenRequest.getClientId();
	}

	protected abstract void handleAfterValidation() throws OAuthProblemException, OAuthSystemException;

}
