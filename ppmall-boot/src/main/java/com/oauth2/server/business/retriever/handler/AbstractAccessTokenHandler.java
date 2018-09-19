package com.oauth2.server.business.retriever.handler;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.UserDetails;
import com.oauth2.server.business.generator.IAuthenticationIdGenerator;
import com.oauth2.server.business.retriever.holder.AbstractOAuthHolder;
import com.oauth2.util.BeanUtil;
public abstract class AbstractAccessTokenHandler extends AbstractOAuthHolder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAccessTokenHandler.class);
    protected UserDetails user = null;

    protected IAuthenticationIdGenerator iAuthenticationIdGenerator = BeanUtil.getBean(IAuthenticationIdGenerator.class);
    
    public AbstractAccessTokenHandler(UserDetails user) {
		// TODO Auto-generated constructor stub
    	this.user = user;
	}
    
    protected AccessToken createAndSaveAccessToken(ClientDetails clientDetails, boolean includeRefreshToken, String username, String authenticationId, UserDetails user, String scope) throws OAuthSystemException {
        Assert.notNull(username, "username is null");
        AccessToken accessToken = new AccessToken()
                .clientId(clientDetails.clientId())
                .username(username)
                .tokenId(oAuthIssuer.accessToken())
                .authenticationId(authenticationId)
                .updateByClientDetails(clientDetails)
                .userObject(user)
                .scope(scope);

        if (includeRefreshToken) {
            accessToken.refreshToken(oAuthIssuer.refreshToken());
        }

        this.iOAuthCacheRepository.saveAccessToken(accessToken);
        LOG.debug("Save AccessToken: {}", accessToken);
        return accessToken;
    }

}
