package com.oauth2.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.oauth2.common.CacheKeyGenerator;
import com.oauth2.common.CacheNames;
import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.repository.AbstractCacheSupport;
import com.oauth2.repository.IOAuthRSCacheRepository;
import com.oauth2.repository.IOAuthRSRepository;

@Repository("IOAuthRSCacheRepository")
public class OAuthRSRedisRepositoryImpl extends AbstractCacheSupport implements IOAuthRSCacheRepository {


    private static final Logger LOG = LoggerFactory.getLogger(OAuthRSRedisRepositoryImpl.class);

    @Autowired
    private IOAuthRSRepository iOAuthRSRepository;

    @Autowired
    private CacheManager cacheManager;


    @Override
    public AccessToken findAccessTokenByTokenId(String tokenId) {

        final Cache accessTokenCache = getAccessTokenCache();

        final String key = CacheKeyGenerator.generateAccessTokenKey(tokenId);
        AccessToken accessToken = (AccessToken) getFromCache(accessTokenCache, key);

        if (accessToken == null) {
            accessToken = iOAuthRSRepository.findAccessTokenByTokenId(tokenId);
            putToCache(accessTokenCache, key, accessToken);
            LOG.debug("Load AccessToken[{}] from DB and cache it, key = {}", accessToken, key);
        }

        return accessToken;
    }

    @Override
    public ClientDetails findClientDetailsByClientIdAndResourceIds(String clientId, String resourceIds) {

        final Cache clientDetailsCache = getClientDetailsCache();

        final String key = CacheKeyGenerator.generateClientDetailsResourceIdsKey(clientId, resourceIds);
        ClientDetails clientDetails = (ClientDetails) getFromCache(clientDetailsCache, key);

        if (clientDetails == null) {
            clientDetails = iOAuthRSRepository.findClientDetailsByClientIdAndResourceIds(clientId, resourceIds);
            putToCache(clientDetailsCache, key, clientDetails);
            LOG.debug("Load ClientDetails[{}] from DB and cache it, key = {}", clientDetails, key);
        }

        return clientDetails;
    }


    private Cache getAccessTokenCache() {
        return cacheManager.getCache(CacheNames.ACCESS_TOKEN_CACHE);
    }

    private Cache getClientDetailsCache() {
        return cacheManager.getCache(CacheNames.CLIENT_DETAILS_CACHE);
    }

}
