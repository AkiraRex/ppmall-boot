package com.ppmall.config.shiro.realm;

import javax.security.auth.login.AccountException;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.oauth2.common.exception.OAuth2AuthenticationException;
import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OAuth2Token;
import com.oauth2.resource.service.IOAuthRSService;

public class RSRedisRealm extends BasicShiroRealm {
	private static final Logger logger = LoggerFactory.getLogger(RSRedisRealm.class);

	@Autowired
    private IOAuthRSService iOAuthRSService;


    public RSRedisRealm() {
        super();
        setAuthenticationTokenClass(OAuth2Token.class);
        logger.info("Initial Resources-Realm: {}, set authenticationTokenClass = {}", this, getAuthenticationTokenClass());
    }


    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    	logger.info("type of token is {}", token.getClass().toString());
    	
        OAuth2Token upToken = (OAuth2Token) token;
        final String accessToken = (String) upToken.getCredentials();

        if (StringUtils.isEmpty(accessToken)) {
            throw new OAuth2AuthenticationException("Invalid access_token: " + accessToken);
        }
        //Validate access token
        AccessToken aToken = iOAuthRSService.loadAccessTokenByTokenId(accessToken);
        validateToken(accessToken, aToken);

        //Validate client details by resource-id
        final ClientDetails clientDetails = iOAuthRSService.loadClientDetails(aToken.clientId(), upToken.getResourceId());
        validateClientDetails(accessToken, aToken, clientDetails);

        String username = aToken.username();

        // Null username is invalid
        if (username == null) {
            try {
				throw new AccountException("Null usernames are not allowed by this realm.");
			} catch (AccountException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return new SimpleAuthenticationInfo(username, accessToken, getName());
    }


    private void validateToken(String token, AccessToken accessToken) throws OAuth2AuthenticationException {
        if (accessToken == null) {
            logger.debug("Invalid access_token: {}, because it is null", token);
            throw new OAuth2AuthenticationException("Invalid access_token: " + token);
        }
        if (accessToken.tokenExpired()) {
            logger.debug("Invalid access_token: {}, because it is expired", token);
            throw new OAuth2AuthenticationException("Invalid access_token: " + token);
        }
    }

    private void validateClientDetails(String token, AccessToken accessToken, ClientDetails clientDetails) throws OAuth2AuthenticationException {
        if (clientDetails == null || clientDetails.archived()) {
            logger.debug("Invalid ClientDetails: {} by client_id: {}, it is null or archived", clientDetails, accessToken.clientId());
            throw new OAuth2AuthenticationException("Invalid client by token: " + token);
        }
    }
}
