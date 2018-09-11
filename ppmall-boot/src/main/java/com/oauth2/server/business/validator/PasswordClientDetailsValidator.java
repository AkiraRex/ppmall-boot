package com.oauth2.server.business.validator;
import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oauth2.domain.ClientDetails;
import com.oauth2.server.business.CustomOAuthTokenRequest;

public class PasswordClientDetailsValidator extends AbstractOauthTokenValidator {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordClientDetailsValidator.class);


    public PasswordClientDetailsValidator(CustomOAuthTokenRequest oauthRequest) {
        super(oauthRequest);
    }

    /*
    * /oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
    * */
    @Override
    protected OAuthResponse validateSelf(ClientDetails clientDetails) throws OAuthSystemException {

        //validate grant_type
        final String grantType = grantType();
        if (!clientDetails.grantTypes().contains(grantType)) {
            LOG.debug("Invalid grant_type '{}', client_id = '{}'", grantType, clientDetails.clientId());
            return invalidGrantTypeResponse(grantType);
        }

        //validate client_secret
        final String clientSecret = oauthRequest.getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.clientSecret())) {
            LOG.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret, clientDetails.clientId());
            return invalidClientSecretResponse();
        }

        //validate scope
        final Set<String> scopes = oauthRequest.getScopes();
        if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
            return invalidScopeResponse();
        }

        //validate username,password
        if (invalidUsernamePassword()) {
            return invalidUsernamePasswordResponse();
        }


        return null;
    }


}
