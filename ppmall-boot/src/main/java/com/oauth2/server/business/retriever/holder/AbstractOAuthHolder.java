package com.oauth2.server.business.retriever.holder;

import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.shiro.SecurityUtils;

import com.oauth2.repository.IOAuthCacheRepository;
import com.oauth2.util.BeanUtil;
import com.ppmall.pojo.User;

public abstract class AbstractOAuthHolder {

	protected IOAuthCacheRepository iOAuthCacheRepository = BeanUtil.getBean(IOAuthCacheRepository.class);

	protected OAuthIssuer oAuthIssuer = BeanUtil.getBean(OAuthIssuer.class);

	/**
	 * Return current login username
	 *
	 * @return Username
	 */
	protected String currentUsername() {
		return ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();
	}
}
