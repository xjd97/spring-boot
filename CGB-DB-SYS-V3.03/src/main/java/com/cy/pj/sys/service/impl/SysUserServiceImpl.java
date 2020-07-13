package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.annotation.RequestLog;
import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

@Transactional(timeout = 30,
			rollbackFor = Throwable.class,
			isolation = Isolation.READ_COMMITTED)
@Service
public class SysUserServiceImpl implements SysUserService {
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private SysUserRoleDao sysUserRoleDao;
	@Resource
	private SysRoleMenuDao sysRoleMenuDao;
	@Resource
	private SysMenuDao sysMenuDao;
	@Resource
	private PageProperties pageProperties;
	
	@RequestLog(operation = "查询用户")
	@Transactional(readOnly = true)
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		Assert.isValid(pageCurrent!=null&&pageCurrent>0, "当前页码不正确");
		int pageSize=pageProperties.getPageSize();
		Page<SysUserDeptVo> page = PageHelper.startPage(pageCurrent, pageSize);
		List<SysUserDeptVo> records = sysUserDao.findPageObjects(username);
		return new PageObject<>(records, (int)page.getTotal(), pageCurrent, pageSize);
	}
	
	/**
	 * 使用@RequiresPermissions 描述业务方法时,
	 * 表示此方法在访问时,需要进行授权(条件是登录用户必须
	 * 具备访问这个方法的权限)
	 */
	@RequiresPermissions("sys:user:update")
	@RequestLog(operation="禁用启用")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		//1.参数校验
		Assert.isValid(id!=null&&id>0, "id值无效");
		Assert.isValid(valid!=null&&(valid==1||valid==0), "状态值无效");
		//2.修改状态
		int rows = sysUserDao.validById(id, valid, modifiedUser);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//3.返回结果
		return rows;
	}
	
	@RequestLog(operation = "保存用户")
	@Override
	public int saveObject(SysUser sysUser, Integer[] roleIds) {
		//1.参数校验
		Assert.notNull(sysUser, "保存对象不能为空");
		Assert.isEmpty(sysUser.getUsername(), "用户名不能为空");
		Assert.isEmpty(sysUser.getPassword(), "密码不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("必须为用户分配角色");
		//2.保存用户自身信息
		//2.1对密码进行加密
		String source = sysUser.getPassword();
		String salt = UUID.randomUUID().toString();
		//String newPassword = DigestUtils.md5DigestAsHex((sysUser.getPassword()+salt).getBytes());
		SimpleHash sh = new SimpleHash(//Shiro框架
				"MD5",//algorithmName 算法名称
				source,//原密码
				salt,//盐值
				1);//hashIterations表示加密次数
		//2.2重新存储到entity对象
		sysUser.setSalt(salt);
		sysUser.setPassword(sh.toHex());//转成16进制保存
		//2.3持久化用户信息
		int rows = sysUserDao.insertObject(sysUser);
		//3.保存用户和角色关系数据
		sysUserRoleDao.insertObjects(sysUser.getId(), roleIds);
		return rows;
	}
	
	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> findObjectById(Integer userId) {
		//1.合法性验证
		Assert.isValid(userId!=null&&userId>0, "参数不合法");
		//2.业务查询
		SysUserDeptVo user = sysUserDao.findObjectById(userId);
		Assert.notNull(user, "此用户已不存在");
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		//3.数据封装
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Override
	public int updateObject(SysUser sysUser, Integer[] roleIds) {
		//1.参数有效性验证
		Assert.notNull(sysUser, "保存对象不能为空");
		Assert.isEmpty(sysUser.getUsername(), "用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("必须为其指定角色");
		//其它验证自己实现，例如用户名已经存在,密码长度,...
		//2.更新用户自身信息
		int rows=sysUserDao.updateObject(sysUser);
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteById("sys_user_roles","user_id",sysUser.getId());
		sysUserRoleDao.insertObjects(sysUser.getId(),roleIds);
		//4.返回结果
		return rows;
	}
	
	@Override
	public int isExists(String columnName, String columnValue) {
		Assert.isEmpty(columnValue, "值不能为空");
		return sysUserDao.isExist("sys_users", columnName, columnValue);
	}

	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		//1.参数校验
		Assert.isEmpty(password, "原密码不能为空");
		Assert.isEmpty(newPassword, "新密码不能为空");
		Assert.isValid(newPassword.equals(cfgPassword), "两次密码输入不一致");
		SysUser user = ShiroUtil.getLoginUser();
		String salt = user.getSalt();
		SimpleHash sh = new SimpleHash("MD5", password, salt,1);
		String hashedPwd = sh.toHex();
		Assert.isValid(user.getPassword().equals(hashedPwd), "原密码不正确");
		//2.更新密码
		String newSalt = UUID.randomUUID().toString();
		String newHashedPwd=new SimpleHash("MD5", cfgPassword, newSalt,1).toHex();
		int rows = sysUserDao.updatePassword(newHashedPwd, newSalt, user.getId());
		if(rows==0)
			throw new ServiceException("更新失败");
		//3.返回结果
		return rows;
	}

	/**
	 * 基于用户id查询用户对应的一级菜单,二级菜单
	 */
	@Override
	public List<SysUserMenuVo> findUserMenusByUserId(Integer id) {
		//1.对用户id进行判断
		Assert.isValid(id!=null&&id>0, "id值无效");
		//2.基于用户id查询用户对应的角色id
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(id);
		//3.基于角色id获取角色对应的菜单信息,并进行封装
		Integer[] array= {};
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		//4.基于菜单id获取菜单信息并返回
		return sysMenuDao.findUserMenusByIds(menuIds);
	}

}
