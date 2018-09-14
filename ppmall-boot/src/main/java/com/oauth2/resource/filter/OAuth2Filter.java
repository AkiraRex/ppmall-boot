package com.oauth2.resource.filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.OAuth2Token;
import com.oauth2.resource.service.IOAuthRSService;
import com.oauth2.util.WebUtils;

@Component
public class OAuth2Filter extends AuthenticatingFilter {


    private static Logger logger = LoggerFactory.getLogger(OAuth2Filter.class);


    //   ParameterStyle.HEADER
    //   private ParameterStyle[] parameterStyles = new ParameterStyle[]{ParameterStyle.QUERY};

    private String resourceId = "ppmall-source";
    
    @Autowired
    private IOAuthRSService iOAuthRSService;


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String accessToken = httpRequest.getHeader(OAuth.HeaderType.AUTHORIZATION);
        // final String accessToken = httpRequest.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
        final AccessToken token = iOAuthRSService.loadAccessTokenByTokenId(accessToken);

        String username = null;
        if (token != null) {
            username = token.username();
            logger.debug("Set username[{}] and clientId[{}] to request that from AccessToken: {}", username, token.clientId(), token);
            httpRequest.setAttribute(OAuth.OAUTH_CLIENT_ID, token.clientId());
        } else {
            logger.debug("Not found AccessToken by access_token: {}", accessToken);
        }

        return new OAuth2Token(accessToken, resourceId)
                .setUserId(username);
    }


    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
//        OAuth2Token oAuth2Token = (OAuth2Token) token;

        final OAuthResponse oAuthResponse;
        try {
            oAuthResponse = OAuthRSResponse.errorResponse(401)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription(ae.getMessage())
                    .buildJSONMessage();

            WebUtils.writeOAuthJsonResponse((HttpServletResponse) response, oAuthResponse);

        } catch (OAuthSystemException e) {
            logger.error("Build JSON message error", e);
            throw new IllegalStateException(e);
        }


        return false;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setRsService(IOAuthRSService iOAuthRSService) {
        this.iOAuthRSService = iOAuthRSService;
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(resourceId, "resourceId is null");
//        Assert.notNull(iOAuthRSService, "iOAuthRSService is null");
//    }
}
