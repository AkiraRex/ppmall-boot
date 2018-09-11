package com.oauth2.common;

public class CacheNames {

	/**
	 * AccessToken cache key: tokenId
	 */
	public static final String ACCESS_TOKEN_CACHE = "accessTokenCache";

	/**
	 * ClientDetails cache key: clientId + cache_name
	 */
	public static final String CLIENT_DETAILS_CACHE = "clientDetailsCache";

	/**
	 * User cache
	 */
	public static final String USERS_CACHE = "usersCache";

	/**
	 * OauthCode cache key: code + clientId
	 */
	public static final String OAUTH_CODE_CACHE = "oauthCodeCache";

	// private
	private CacheNames() {
	}

}
