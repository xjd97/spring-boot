package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleMenuDao extends BaseDao{
	/**
	 * 基于菜单id删除角色和菜单关系数据
	 * @param id
	 * @return
	 */
//	@Delete("delete from sys_role_menus where menu_id=#{id}")
//	int deleteObjectsByMenuId(Integer id);
	
	/**
	 * 基于角色id删除角色和菜单关系数据
	 * @param id
	 * @return
	 */
//	@Delete("delete from sys_role_menus where role_id=#{id}")
//	int deleteObjectsByRoleId(Integer id);
	
	int insertObjects(
			@Param("roleId") Integer roleId,
			@Param("menuIds") Integer[] menuIds);
	
	List<Integer> findMenuIdsByRoleId(Integer id);
	
	List<Integer> findMenuIdsByRoleIds(
			@Param("roleIds")Integer[] roleIds);
}
