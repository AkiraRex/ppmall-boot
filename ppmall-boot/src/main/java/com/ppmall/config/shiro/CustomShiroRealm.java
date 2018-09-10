package com.ppmall.config.shiro;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import com.ppmall.common.Const;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;

public class CustomShiroRealm extends AuthorizingRealm {

	@Resource
	public IUserService iUserServiceImpl;

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
		String username = token.getPrincipal().toString();

		User user = (User) iUserServiceImpl.loadUserByUsername(username).getData();

		String password = user.getPassword();
		
		if (password != null) {
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, // 认证通过后，存放在session,一般存放user对象
					password, // 用户数据库中的密码
					getName()); // 返回Realm名
			return authenticationInfo;
		}
		return null;
	}

}
