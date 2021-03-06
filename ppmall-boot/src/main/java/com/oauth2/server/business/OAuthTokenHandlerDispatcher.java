package com.oauth2.server.business;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.handler.OAuthTokenHandler;
import com.oauth2.server.business.handler.impl.OAuthPasswordTokenHandler;
import com.oauth2.server.business.handler.impl.OAuthRefreshTokenHandler;
import com.oauth2.server.business.service.IUserDetailsService;
import com.oauth2.util.BeanUtil;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class OAuthTokenHandlerDispatcher {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthTokenHandlerDispatcher.class);

	IUserDetailsService iUserService = BeanUtil.getBean(IUserDetailsService.class);
	
    private List<OAuthTokenHandler> handlers = new ArrayList<>();

    private final CustomOAuthTokenRequest tokenRequest;
    private final HttpServletResponse response;

    public OAuthTokenHandlerDispatcher(CustomOAuthTokenRequest tokenRequest, HttpServletResponse response) {
        this.tokenRequest = tokenRequest;
        this.response = response;

        initialHandlers();
    }

    private void initialHandlers() {
    	
    	
    	
//      handlers.add(new AuthorizationCodeTokenHandler());
        handlers.add(new OAuthPasswordTokenHandler());
        handlers.add(new OAuthRefreshTokenHandler());

//      handlers.add(new ClientCredentialsTokenHandler());

        LOG.debug("Initialed '{}' OAuthTokenHandler(s): {}", handlers.size(), handlers);
    }


    public void dispatch() throws OAuthProblemException, OAuthSystemException {
    	String username = tokenRequest.getUsername();
    	UserDetails user = iUserService.loadUserByUsername(username);
       
    	for (OAuthTokenHandler handler : handlers) {
            if (handler.support(tokenRequest)) {
                LOG.debug("Found '{}' handle OAuthTokenxRequest: {}", handler, tokenRequest);
                handler.handle(tokenRequest, response, user);
                return;
            }
        }
        throw new IllegalStateException("Not found 'OAuthTokenHandler' to handle OAuthTokenxRequest: " + tokenRequest);
    }
	

}
