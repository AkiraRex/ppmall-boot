package com.oauth2.repository.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.oauth2.common.CacheKeyGenerator;
import com.oauth2.common.CacheNames;
import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OauthCode;
import com.oauth2.repository.AbstractCacheSupport;
import com.oauth2.repository.IOAuthCacheRepository;
import com.oauth2.repository.IOAuthRepository;

@Component("iOAuthCacheRepository")
public class OAuthCacheRepositoryImpl extends AbstractCacheSupport implements IOAuthCacheRepository {

	private static final Logger LOG = LoggerFactory.getLogger(OAuthCacheRepositoryImpl.class);

    @Autowired
    private IOAuthRepository iOAuthRepository;

    @Autowired
    private CacheManager cacheManager;


    @Override
    public int saveOauthCode(OauthCode oauthCode) {

        //cache to redis
        final Cache cache = getOauthCodeCache();
        final String key = CacheKeyGenerator.generateOauthCodeKey(oauthCode);
        final String key1 = CacheKeyGenerator.generateOauthCodeUsernameClientIdKey(oauthCode);

        putToCache(cache, key, oauthCode);
        putToCache(cache, key1, oauthCode);
        LOG.debug("Cache OauthCode[{}], key = {}, key1 = {}", oauthCode, key, key1);

        //persist to DB
        return iOAuthRepository.saveOauthCode(oauthCode);
    }

    @Override
    public OauthCode findOauthCodeByUsernameClientId(String username, String clientId) {
        final Cache oauthCodeCache = getOauthCodeCache();

        //try to get from cache
        final String key = CacheKeyGenerator.generateOauthCodeUsernameClientIdKey(username, clientId);
        OauthCode oauthCode = (OauthCode) getFromCache(oauthCodeCache, key);

        if (oauthCode == null) {
            oauthCode = iOAuthRepository.findOauthCodeByUsernameClientId(username, clientId);
            final boolean result = putToCache(oauthCodeCache, key, oauthCode);
            LOG.debug("Load OauthCode[{}] from DB and cache it, username = {},clientId = {} result: {}", oauthCode, username, clientId, result);
        }
        return oauthCode;
    }

    @Override
    public int deleteOauthCode(OauthCode oauthCode) {

        //clean cache
        final Cache oauthCodeCache = getOauthCodeCache();
        final String key = CacheKeyGenerator.generateOauthCodeUsernameClientIdKey(oauthCode);
        final String key1 = CacheKeyGenerator.generateOauthCodeKey(oauthCode);

        evictFromCache(oauthCodeCache, key);
        evictFromCache(oauthCodeCache, key1);
        LOG.debug("Evict OauthCode[{}] cache values, key = {}, key1 = {}", oauthCode, key, key1);

        return iOAuthRepository.deleteOauthCode(oauthCode);
    }

    @Override
    public int saveAccessToken(AccessToken accessToken) {

        //add to cache
        final String key = CacheKeyGenerator.generateAccessTokenKey(accessToken);
        final String key1 = CacheKeyGenerator.generateAccessTokenUsernameClientIdAuthIdKey(accessToken);

        final Cache accessTokenCache = getAccessTokenCache();
        putToCache(accessTokenCache, key, accessToken);
        putToCache(accessTokenCache, key1, accessToken);
        LOG.debug("Cache AccessToken[{}], key = {}, key1 = {}", accessToken, key, key1);

        //refresh cache
        if (StringUtils.isNotEmpty(accessToken.refreshToken())) {
            final String key2 = CacheKeyGenerator.generateAccessTokenRefreshKey(accessToken);
            putToCache(accessTokenCache, key2, accessToken);
            LOG.debug("Cache AccessToken[{}] by refresh-token, key = {}", accessToken, key2);
        }

        return iOAuthRepository.saveAccessToken(accessToken);
    }

    @Override
    public AccessToken findAccessToken(String clientId, String username, String authenticationId) {
        final Cache accessTokenCache = getAccessTokenCache();

        final String key = CacheKeyGenerator.generateAccessTokenUsernameClientIdAuthIdKey(username, clientId, authenticationId);
        AccessToken accessToken = (AccessToken) getFromCache(accessTokenCache, key);

        if (accessToken == null) {
            accessToken = iOAuthRepository.findAccessToken(clientId, username, authenticationId);
            putToCache(accessTokenCache, key, accessToken);
            LOG.debug("Load AccessToken[{}] from DB and cache it, clientId = {}, username = {}, authenticationId = {}", accessToken, clientId, username, authenticationId);
        }

        return accessToken;
    }

    @Override
    public int deleteAccessToken(AccessToken accessToken) {

        //clean from cache
        final String key = CacheKeyGenerator.generateAccessTokenKey(accessToken);
        final String key1 = CacheKeyGenerator.generateAccessTokenUsernameClientIdAuthIdKey(accessToken);

        final Cache accessTokenCache = getAccessTokenCache();
        evictFromCache(accessTokenCache, key);
        evictFromCache(accessTokenCache, key1);
        LOG.debug("Evict AccessToken[{}] from cache, key = {}, key1 = {}", accessToken, key, key1);

        return iOAuthRepository.deleteAccessToken(accessToken);
    }

    @Override
    public OauthCode findOauthCode(String code, String clientId) {

        final Cache oauthCodeCache = getOauthCodeCache();

        final String key = CacheKeyGenerator.generateOauthCodeKey(code, clientId);
        OauthCode oauthCode = (OauthCode) getFromCache(oauthCodeCache, key);

        if (oauthCode == null) {
            oauthCode = iOAuthRepository.findOauthCode(code, clientId);
            putToCache(oauthCodeCache, key, oauthCode);
            LOG.debug("Load OauthCode[{}] from DB and cache it, code = {}, clientId = {}", oauthCode, code, clientId);
        }

        return oauthCode;
    }

    @Override
    public AccessToken findAccessTokenByRefreshToken(String refreshToken, String clientId) {

        final Cache accessTokenCache = getAccessTokenCache();

        final String key = CacheKeyGenerator.generateAccessTokenRefreshKey(refreshToken, clientId);
        AccessToken accessToken = (AccessToken) getFromCache(accessTokenCache, key);

        if (accessToken == null) {
            accessToken = iOAuthRepository.findAccessTokenByRefreshToken(refreshToken, clientId);
            putToCache(accessTokenCache, key, accessToken);
            LOG.debug("Load AccessToken[{}] from DB and cache it, refreshToken = {}, clientId = {}", accessToken, refreshToken, clientId);
        }

        return accessToken;
    }

    @Override
    public ClientDetails findClientDetails(String clientId) {

        final Cache clientDetailsCache = getClientDetailsCache();

        final String key = CacheKeyGenerator.generateClientDetailsKey(clientId);
        ClientDetails clientDetails = (ClientDetails) getFromCache(clientDetailsCache, key);

        if (clientDetails == null) {
            clientDetails = iOAuthRepository.findClientDetails(clientId);
            putToCache(clientDetailsCache, key, clientDetails);
            LOG.debug("Load ClientDetails[{}] from DB and cache it, clientId = {}", clientDetails, clientId);
        }

        return clientDetails;
    }


    private Cache getOauthCodeCache() {
        return cacheManager.getCache(CacheNames.OAUTH_CODE_CACHE);
    }

    private Cache getAccessTokenCache() {
        return cacheManager.getCache(CacheNames.ACCESS_TOKEN_CACHE);
    }

    private Cache getClientDetailsCache() {
        return cacheManager.getCache(CacheNames.CLIENT_DETAILS_CACHE);
    }


}
