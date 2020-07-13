package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleDao {
	/**
	 * 基于条件(模糊查询)统计角色总数
	 * @param name
	 * @return
	 */
	int getRowCount(@Param("name") String name);
	
	/**
	 * 基于条件查询当前页呈现的记录
	 * @param name
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<SysRole> findPageObjects(
				@Param("name") String name,
				@Param("startIndex") Integer startIndex,
				@Param("pageSize") Integer pageSize);
	
	/**
	 * 基于条件删除角色信息
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 添加角色信息
	 * @param sysRole
	 * @return
	 */
	int insertObject(SysRole sysRole);
	
	/**
	 * 基于id查询vo对象
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);
	
	/**
	 * 更新角色信息
	 * @param sysRole
	 * @return
	 */
	int updateObject(SysRole sysRole);
	
	/**
	 *查询所有角色id,name
	 * @return
	 */
	List<CheckBox> findObjects();
}
