package com.oauth2.repository;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;

public interface IOAuthRSRepository {
	AccessToken findAccessTokenByTokenId(String tokenId);

	ClientDetails findClientDetailsByClientIdAndResourceIds(String clientId, String resourceIds);
}
