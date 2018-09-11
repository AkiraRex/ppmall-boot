package com.oauth2.server.business.retriever.handler;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.server.business.generator.IAuthenticationIdGenerator;
import com.oauth2.server.business.retriever.holder.AbstractOAuthHolder;
public abstract class AbstractAccessTokenHandler extends AbstractOAuthHolder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAccessTokenHandler.class);


    @Autowired
    protected IAuthenticationIdGenerator iAuthenticationIdGenerator;
    
    protected AccessToken createAndSaveAccessToken(ClientDetails clientDetails, boolean includeRefreshToken, String username, String authenticationId) throws OAuthSystemException {
        Assert.notNull(username, "username is null");
        AccessToken accessToken = new AccessToken()
                .clientId(clientDetails.clientId())
                .username(username)
                .tokenId(oAuthIssuer.accessToken())
                .authenticationId(authenticationId)
                .updateByClientDetails(clientDetails);

        if (includeRefreshToken) {
            accessToken.refreshToken(oAuthIssuer.refreshToken());
        }

        this.iOAuthRepository.saveAccessToken(accessToken);
        LOG.debug("Save AccessToken: {}", accessToken);
        return accessToken;
    }

}
