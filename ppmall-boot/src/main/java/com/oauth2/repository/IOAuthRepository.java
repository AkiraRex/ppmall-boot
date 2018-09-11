package com.oauth2.repository;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;

public interface IOAuthRepository {
	ClientDetails findClientDetails(String clientId);

    int saveClientDetails(ClientDetails clientDetails);

    int saveOauthCode(OauthCode oauthCode);

    OauthCode findOauthCode(String code, String clientId);

    OauthCode findOauthCodeByUsernameClientId(String username, String clientId);

    int deleteOauthCode(OauthCode oauthCode);

    AccessToken findAccessToken(String clientId, String username, String authenticationId);

    int deleteAccessToken(AccessToken accessToken);

    int saveAccessToken(AccessToken accessToken);

    AccessToken findAccessTokenByRefreshToken(String refreshToken, String clientId);
}
