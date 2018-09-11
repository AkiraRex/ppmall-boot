package com.oauth2.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;


public abstract class AbstractCacheSupport {
	 private static final Logger LOG = LoggerFactory.getLogger(AbstractCacheSupport.class);


	    /**
	     * 从指定的缓存中获取缓存数据
	     *
	     * @param cache Cache
	     * @param key   Cache key
	     * @return Cache value
	     */
	    protected Object getFromCache(Cache cache, String key) {
	        final Cache.ValueWrapper valueWrapper = cache.get(key);
	        return valueWrapper == null ? null : valueWrapper.get();
	    }


	    /**
	     * 向缓存中添加 缓存数据
	     *
	     * @param cache Cache
	     * @param key   Cache key
	     * @param value Cache value, null will return false
	     * @return True is successful,otherwise false
	     */
	    protected boolean putToCache(Cache cache, Object key, Object value) {
	        if (value == null) {
	            LOG.debug("Ignore put to cache[{}], key = {}, because value is null", cache, key);
	            return false;
	        }
	        cache.put(key, value);
	        LOG.debug("Put [{} = {}] to cache[{}]", key, value, cache);
	        return true;
	    }


	    /**
	     * 清除缓存中具体的 缓存数据
	     *
	     * @param cache Cache
	     * @param key   Cache key
	     * @return True is evict successful
	     */
	    protected boolean evictFromCache(Cache cache, Object key) {
	        if (key == null) {
	            LOG.debug("Ignore evict from cache[{}], because key is null", cache);
	            return false;
	        }
	        cache.evict(key);
	        LOG.debug("Evict key[{}] from cache[{}]", key, cache);
	        return true;
	    }

}
