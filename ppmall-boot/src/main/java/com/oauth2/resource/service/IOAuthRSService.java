package com.oauth2.resource.service;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;

public interface IOAuthRSService {
	AccessToken loadAccessTokenByTokenId(String tokenId);

	ClientDetails loadClientDetails(String clientId, String resourceIds);
}
