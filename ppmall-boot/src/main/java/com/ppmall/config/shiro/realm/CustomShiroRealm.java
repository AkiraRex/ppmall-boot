package com.ppmall.config.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.security.auth.login.AccountException;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import com.oauth2.common.exception.OAuth2AuthenticationException;
import com.oauth2.domain.AccessToken;
import com.oauth2.domain.ClientDetails;
import com.oauth2.domain.OAuth2Token;
import com.oauth2.resource.service.IOAuthRSService;
import com.ppmall.common.Const;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;

public class CustomShiroRealm extends AuthorizingRealm {
	private static final Logger logger = LoggerFactory.getLogger(CustomShiroRealm.class);

	@Autowired
	private IUserService iUserService;
	
	@Autowired
    private IOAuthRSService iOAuthRSService;
	
	public CustomShiroRealm() {
		// TODO Auto-generated constructor stub
		setAuthenticationTokenClass(OAuth2Token.class);
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		int role = user.getRole();
		String roles = ""; 
		switch (role) {
		case Const.Role.ROLE_ADMIN:
			roles = "ROLE_ADMIN";
			break;
		case Const.Role.ROLE_CUSTOMER:
			roles = "ROLE_USERS";
		default:
			break;
		}
		Set<String> roleSet = new HashSet<String>();
		roleSet.add(roles);
		authorizationInfo.setRoles(roleSet);
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取用户账号
//		String username = token.getPrincipal().toString();
//
//		User user = (User) iUserService.loadUserByUsername(username).getData();
//
//		String password = user.getPassword();
//		
//		if (password != null) {
//			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, // 认证通过后，存放在session,一般存放user对象
//					password, // 用户数据库中的密码
//					getName()); // 返回Realm名
//			return authenticationInfo;
//		}
//		return null;
		
		OAuth2Token upToken = (OAuth2Token) token;
		final String accessToken = (String) upToken.getCredentials();

		if (StringUtils.isEmpty(accessToken)) {
			throw new OAuth2AuthenticationException("Invalid access_token: " + accessToken);
		}
		// Validate access token
		AccessToken aToken = iOAuthRSService.loadAccessTokenByTokenId(accessToken);
		validateToken(accessToken, aToken);

		// Validate client details by resource-id
		final ClientDetails clientDetails = iOAuthRSService.loadClientDetails(aToken.clientId(),
				upToken.getResourceId());
		validateClientDetails(accessToken, aToken, clientDetails);

		String username = aToken.username();

		// Null username is invalid
		if (username == null) {
			try {
				throw new AccountException("Null usernames are not allowed by this realm.");
			} catch (AccountException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new SimpleAuthenticationInfo(username, accessToken, getName());
	}

	private void validateToken(String token, AccessToken accessToken) throws OAuth2AuthenticationException {
		if (accessToken == null) {
			logger.debug("Invalid access_token: {}, because it is null", token);
			throw new OAuth2AuthenticationException("Invalid access_token: " + token);
		}
		if (accessToken.tokenExpired()) {
			logger.debug("Invalid access_token: {}, because it is expired", token);
			throw new OAuth2AuthenticationException("Invalid access_token: " + token);
		}
    }

    private void validateClientDetails(String token, AccessToken accessToken, ClientDetails clientDetails) throws OAuth2AuthenticationException {
        if (clientDetails == null || clientDetails.archived()) {
            logger.debug("Invalid ClientDetails: {} by client_id: {}, it is null or archived", clientDetails, accessToken.clientId());
            throw new OAuth2AuthenticationException("Invalid client by token: " + token);
        }
    }

}
