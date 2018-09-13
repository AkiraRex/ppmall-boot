package com.oauth2.repository.impl;

import org.springframework.stereotype.Repository;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.repository.IOAuthRSRepository;

@Repository("iOAuthRSRepository")
public class OAuthRSRepositoryImpl implements IOAuthRSRepository {

	@Override
	public AccessToken findAccessTokenByTokenId(String tokenId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDetails findClientDetailsByClientIdAndResourceIds(String clientId, String resourceIds) {
		// TODO Auto-generated method stub
		ClientDetails clientDetails = new ClientDetails();
		clientDetails
			.clientId("77ace8df917")
			.name("password_client")
			.clientSecret("c69368e339c40d")
			.grantTypes("password refresh_token")
			.scope("read");
		return clientDetails;
	}


  
}
