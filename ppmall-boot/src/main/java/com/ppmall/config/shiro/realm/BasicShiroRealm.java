package com.ppmall.config.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.AccountException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;

public class BasicShiroRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(BasicShiroRealm.class);

	@Autowired
	private IUserService iUserService;

	public BasicShiroRealm() {
	        super();
	    }

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		String username = getUsername(token);

		// Null username is invalid
		if (username == null) {
			try {
				throw new AccountException("Null username are not allowed by this realm.");
			} catch (AccountException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		ServerResponse<User> serverResponse = iUserService.loadUserByUsername(username);
		User user = (User) serverResponse.getData();
		logger.debug("Load Users[{}] by username: {}", user, username);

		AuthenticationInfo info = null;
		if (user != null) {
			info = new SimpleAuthenticationInfo(user,user.getPassword(), getName());
		}

		return info;
	}

	protected String getUsername(AuthenticationToken token) {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		return upToken.getUsername();
	}

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

	protected String getUsername(PrincipalCollection principals) {
		// null usernames are invalid
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}

		return (String) getAvailablePrincipal(principals);
	}
//
//	public void setIUserService(IUserService iUserService) {
//		this.iUserService = iUserService;
//	}

}
