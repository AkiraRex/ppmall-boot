package com.oauth2.repository;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;

public interface IOAuthCacheRepository {

    int saveOauthCode(OauthCode oauthCode);

    OauthCode findOauthCodeByUsernameClientId(String username, String clientId);

    int deleteOauthCode(OauthCode oauthCode);

    int saveAccessToken(AccessToken accessToken);

    AccessToken findAccessToken(String clientId, String username, String authenticationId);

    int deleteAccessToken(AccessToken accessToken);

    OauthCode findOauthCode(String code, String clientId);

    AccessToken findAccessTokenByRefreshToken(String refreshToken, String clientId);

    ClientDetails findClientDetails(String clientId);
}
