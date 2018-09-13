package com.oauth2.server.business.retriever;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.server.business.retriever.handler.AbstractAccessTokenHandler;
public class AccessTokenRetriever extends AbstractAccessTokenHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenRetriever.class);

    private final ClientDetails clientDetails;
    private final Set<String> scopes;
    private final boolean includeRefreshToken;

    public AccessTokenRetriever(ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken) {
        this.clientDetails = clientDetails;
        this.scopes = scopes;
        this.includeRefreshToken = includeRefreshToken;
    }

    public AccessToken retrieve() throws OAuthSystemException {

        String scope = OAuthUtils.encodeScopes(scopes);
        final String username = currentUsername();
        final String clientId = clientDetails.clientId();

        final String authenticationId = iAuthenticationIdGenerator.generate(clientId, username, scope);

        AccessToken accessToken = iOAuthCacheRepository.findAccessToken(clientId, username, authenticationId);
        if (accessToken == null) {
            accessToken = createAndSaveAccessToken(clientDetails, includeRefreshToken, username, authenticationId);
            LOG.debug("Create a new AccessToken: {}", accessToken);
        }

        return accessToken;
    }
}
