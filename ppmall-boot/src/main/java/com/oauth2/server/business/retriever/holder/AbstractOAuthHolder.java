package com.oauth2.server.business.retriever.holder;

import org.springframework.beans.factory.annotation.Autowired;

import com.oauth2.repository.IOAuthCacheRepository;
import com.ppmall.pojo.User;

import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.shiro.SecurityUtils;

public abstract class AbstractOAuthHolder {

	@Autowired
	protected IOAuthCacheRepository iOAuthRepository;

	@Autowired
	protected OAuthIssuer oAuthIssuer;

	/**
	 * Return current login username
	 *
	 * @return Username
	 */
	protected String currentUsername() {
		return ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();
	}
}
