package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Mapper
public interface SysMenuDao {
	/**
	 * 统计当前菜单中子菜单的数量
	 * @param id 当前菜单id
	 * @return 子菜单数量
	 */
	@Select("select count(*) from sys_menus where parentId=#{id}")
	int getChildCount(Integer id);
	
	/**
	 * 基于id删除当前菜单
	 * @param id
	 * @return
	 */
	@Delete("delete from sys_menus where id=#{id}")
	int deleteObject(Integer id);
	
	/**
	 * 查询所有菜单以及对应的上级菜单名称
	 * 一行记录映射为一个map对象(K为表中字段名,V为每行记录字段对应的值)
	 */
	List<Map<String,Object>> findObjects();
	
	/**
	 * 数据的持久化
	 * @param sysMenu 被持久化的对象
	 * @return 更新的行数
	 */
	int insertObject(SysMenu sysMenu);
	
	/**
	 * 数据的持久化
	 * @param sysMenu 被持久化的对象
	 * @return 更新的行数
	 */
	int updateObject(SysMenu sysMenu);
	
	/**
	 * 查询菜单节点信息(包含id,name,parentId)
	 * @return
	 */
	@Select("select id,name,parentId from sys_menus")
	List<Node> findZtreeMenuNodes();
	
	/**
	 * 基于多个id获取菜单授权标识
	 * @param menuIds
	 * @return
	 */
	List<String> findPermissions(
			@Param("menuIds")
			Integer[] menuIds);
	
	/**
	 * 基于菜单id获取菜单信息
	 * @param menuIds
	 * @return
	 */
	List<SysUserMenuVo> findUserMenusByIds(@Param("menuIds")List<Integer> menuIds);
}
