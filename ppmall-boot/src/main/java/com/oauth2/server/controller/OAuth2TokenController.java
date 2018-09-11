package com.oauth2.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oauth2.server.business.CustomOAuthTokenRequest;
import com.oauth2.server.business.OAuthTokenHandlerDispatcher;
import com.oauth2.util.WebUtils;

@Controller
@RequestMapping("/oauth")
public class OAuth2TokenController {
	/**
     * Handle grant_types as follows:
     * <p/>
     * grant_type=authorization_code
     * grant_type=password
     * grant_type=refresh_token
     * grant_type=client_credentials
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
	@RequestMapping("/token")
	@ResponseBody
	public void authorize(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CustomOAuthTokenRequest tokenRequest = new CustomOAuthTokenRequest(request);

			OAuthTokenHandlerDispatcher tokenHandleDispatcher = new OAuthTokenHandlerDispatcher(tokenRequest, response);
			tokenHandleDispatcher.dispatch();

		} catch (OAuthProblemException e) {
			// exception
			OAuthResponse oAuthResponse = 
					OAuthASResponse
						.errorResponse(HttpServletResponse.SC_FOUND)
						.location(e.getRedirectUri())
						.error(e)
						.buildJSONMessage();
			WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
		}
	}
}
