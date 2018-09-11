package com.oauth2.server.business;

import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class CustomOAuthTokenRequest extends OAuthTokenRequest {
	/**
	 * Create an OAuth Token request from a given HttpSerlvetRequest
	 *
	 * @param request
	 *            the HttpServletRequest that is validated and transformed into
	 *            the OAuth Token Request
	 * @throws org.apache.oltu.oauth2.common.exception.OAuthSystemException
	 *             if an unexpected exception was thrown
	 * @throws org.apache.oltu.oauth2.common.exception.OAuthProblemException
	 *             if the request was not a valid Token request this exception
	 *             is thrown.
	 */
	public CustomOAuthTokenRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
		super(request);
		// TODO Auto-generated constructor stub
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}
}
