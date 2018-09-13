package com.oauth2.resource.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.repository.IOAuthRSCacheRepository;
import com.oauth2.resource.service.IOAuthRSService;

@Service("iOAuthRSService")
public class OAuthRSServiceImpl implements IOAuthRSService{

	 private static final Logger LOG = LoggerFactory.getLogger(OAuthRSServiceImpl.class);


	    @Autowired
	    private IOAuthRSCacheRepository iOAuthRSRepository;


	    @Override
	    public AccessToken loadAccessTokenByTokenId(String tokenId) {
	        return iOAuthRSRepository.findAccessTokenByTokenId(tokenId);
	    }


	    @Override
	    public ClientDetails loadClientDetails(String clientId, String resourceIds) {
	        LOG.debug("Load ClientDetails by clientId: {}, resourceIds: {}", clientId, resourceIds);
	        return iOAuthRSRepository.findClientDetailsByClientIdAndResourceIds(clientId, resourceIds);
	    }


}
