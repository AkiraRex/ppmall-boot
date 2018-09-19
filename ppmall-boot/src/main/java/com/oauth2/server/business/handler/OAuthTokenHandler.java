package com.oauth2.server.business.handler;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.CustomOAuthTokenRequest;

public interface OAuthTokenHandler {
	boolean support(CustomOAuthTokenRequest tokenRequest) throws OAuthProblemException;

	void handle(CustomOAuthTokenRequest tokenRequest, HttpServletResponse response, UserDetails user) throws OAuthProblemException, OAuthSystemException;
}
