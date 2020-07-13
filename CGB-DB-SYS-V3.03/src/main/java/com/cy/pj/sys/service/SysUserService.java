package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;

public interface SysUserService {
	PageObject<SysUserDeptVo> findPageObjects(
			 String username,Integer pageCurrent);
	
	int validById(Integer id,Integer valid,String modifiedUser);
	
	/**
	 * 保存用户以及用户对应的关系数据
	 * @param sysUser
	 * @param roleIds
	 * @return
	 */
	int saveObject(SysUser sysUser,Integer[]roleIds);
	
	/**
	 * 基于用户id获取用户以及用户对应的关系数据
	 * @param userId
	 * @return
	 */
	Map<String,Object> findObjectById(Integer userId);
	
	int updateObject(SysUser sysUser,Integer[] roleIds);
	
	int isExists(String columnName,String columnValue);
	
	/**
	 * 修改登录用户密码
	 * @param password
	 * @param newPassword
	 * @param cfgPassword
	 * @return
	 */
	int updatePassword(String password,String newPassword,String cfgPassword);
	
	List<SysUserMenuVo> findUserMenusByUserId(Integer id);
}
