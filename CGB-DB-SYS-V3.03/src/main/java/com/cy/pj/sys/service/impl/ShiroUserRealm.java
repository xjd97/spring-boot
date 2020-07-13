package com.cy.pj.sys.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class ShiroUserRealm extends AuthorizingRealm{
	@Autowired
	private SysUserDao sysUserDao;
	@SuppressWarnings("unused")
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@SuppressWarnings("unused")
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@SuppressWarnings("unused")
	@Autowired
	private SysMenuDao sysMenuDao;
	
	/**
	 * 设置凭证匹配器(与用户添加操作使用相同的加密算法)
	 */
	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		//构建凭证匹配对象
		HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
		//设置加密算法
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密次数
		cMatcher.setHashIterations(1);
		return cMatcher;
	}
	
	/**
	 * 此方法负责完成认证信息的获取以及封装
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取用户名(用户页面输入)
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		String username = upToken.getUsername();
		//2.基于用户名查询用户信息
		SysUser user = sysUserDao.findUserByUserName(username);
		//3.判定用户是否存在
		if(user==null)
			throw new UnknownAccountException();
		//4.判定用户是否已被禁用
		if(user.getValid()==0)
			throw new LockedAccountException();
		//5.封装用户信息
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt().getBytes());
		//构建什么对象要看方法的返回值
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user,//principal (身份)
				user.getPassword(),//hashedCredentials
				credentialsSalt,//credentialsSalt
				getName());//realmName
		//返回封装结果
		return info;
		//返回值会传递给认证管理器(SecurityManager)(后续认证管理器会通过此信息完成认证操作)
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1.获取登录用户信息,例如用户id
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		Integer userId = user.getId();
		//2.基于用户id获取用户权限
		List<String> permissions = sysUserDao.findUserPermissions(userId);
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//3.封装查询结果
		Set<String> set = new HashSet<>();
		for (String per : permissions) {
			if(!StringUtils.isEmpty(per)) {
				set.add(per);
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		return info;//返回给授权管理器
	}

	/**通过此方法完成授权信息的获取及封装*/
	/*方案一
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1.获取登录用户信息,例如用户id
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		Integer userId = user.getId();
		//2.基于用户id获取用户拥有的角色
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于角色id获取菜单id
		Integer[] array = {};//定义整数数组类型
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id获取权限标识
		List<String> permissions = sysMenuDao.findPermissions(menuIds.toArray(array));
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.对权限标识信息进行封装并返回
		Set<String> set = new HashSet<>();
		for (String per : permissions) {
			if(!StringUtils.isEmpty(per)) {
				set.add(per);
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		return info;//返回给授权管理器
	}*/
	
}
