package com.ppmall.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.oauth2.util.WebUtils;

/**
 * 自定义异常处理
 * 
 * @author rex
 *
 */
public class CustomShiroExceptionResolver implements HandlerExceptionResolver {

	public CustomShiroExceptionResolver() {
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		if (ex instanceof UnauthorizedException) {
			handleUnauthorizedException(response, ex);
		} else if (ex instanceof AuthorizationException) {
			handleUnauthorizedException(response, ex);
		} else {
			ex.printStackTrace();
		}
		// more

		return null;
	}

	private void handleUnauthorizedException(HttpServletResponse response, Exception ex) {
		OAuthResponse oAuthResponse;
		try {
			oAuthResponse = OAuthASResponse.errorResponse(403).setError(OAuthError.ResourceResponse.INVALID_TOKEN)
					.setErrorDescription(ex.getMessage()).buildJSONMessage();
		} catch (OAuthSystemException e) {
			throw new IllegalStateException(e);
		}

		WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
	}

}
