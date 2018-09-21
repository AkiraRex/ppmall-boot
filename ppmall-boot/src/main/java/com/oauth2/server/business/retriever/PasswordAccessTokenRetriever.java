package com.oauth2.server.business.retriever;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.retriever.handler.AbstractAccessTokenHandler;


public class PasswordAccessTokenRetriever extends AbstractAccessTokenHandler {


    private static final Logger LOG = LoggerFactory.getLogger(PasswordAccessTokenRetriever.class);


    private final ClientDetails clientDetails;
    private final Set<String> scopes;
    private final String username;
   

    public PasswordAccessTokenRetriever(ClientDetails clientDetails, Set<String> scopes, String username, UserDetails user) {
    	super(user);
    	this.clientDetails = clientDetails;
        this.scopes = scopes;
        this.username = username;
    }

    //grant_type=password AccessToken
    public AccessToken retrieve() throws OAuthSystemException {

        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.clientId();

        final String authenticationId = iAuthenticationIdGenerator.generate(clientId, username, scope);
//        AccessToken accessToken = iOAuthCacheRepository.findAccessToken(clientId, username, authenticationId);

//        boolean needCreated = needCreated(clientId, accessToken);
//        
//        needCreated = true;
//
//        if (needCreated) {
//            accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId, user, scope);
//            LOG.info("Create a new AccessToken: {}", accessToken);
//        }
        
        AccessToken accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId, user, scope);
        LOG.info("Create a new AccessToken: {}", accessToken);

        return accessToken;
    }

    private boolean needCreated(String clientId, AccessToken accessToken) {
        boolean needCreate = false;

        if (accessToken == null) {
            needCreate = true;
            LOG.debug("Not found AccessToken from repository, will create a new one, client_id: {}", clientId);
        } else if (accessToken.tokenExpired()) {
            LOG.debug("Delete expired AccessToken: {} and create a new one, client_id: {}", accessToken, clientId);
            iOAuthCacheRepository.deleteAccessToken(accessToken);
            needCreate = true;
        } else {
            LOG.debug("Use existed AccessToken: {}, client_id: {}", accessToken, clientId);
        }
        return needCreate;
    }
}

