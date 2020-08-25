package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

import javax.annotation.Resource;

@Transactional
@Service
public class SysMenuServiceImpl implements SysMenuService{
	@Resource
	private SysMenuDao sysMenuDao;
	@Resource
	private SysRoleMenuDao sysRoleMenuDao;
	
	/**
	 * @Cacheable 描述方法时,表示要从cache取数据,cache没有,调用业务方法查数据
	 * 查到数据放到cache中
	 * 1)value表示cache的名字
	 */
	@Cacheable(value = "menuCache")//Spring框架负责提供Cache对象
	@Override
	public List<Map<String, Object>> findObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		if(list==null||list.size()==0)
			throw new ServiceException("没有对应的菜单信息");
		return list;
	}

	/**
	 * @CacheEvict 描述方法时,表示要清除缓存
	 * 1)value:表示缓存名称
	 * 2)allEntries:表示清楚缓存中所有对象
	 */
	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int deleteObject(Integer id) {
		//1.判断参数有效性
		Assert.isValid(id!=null&&id>1, "id值无效");
		//2.查询当前菜单是否有子菜单,并进行校验
		int childCount = sysMenuDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("请先删除子菜单");
		//3.删除角色菜单关系数据
		sysRoleMenuDao.deleteById("sys_role_menus","menu_id",id);
		//4.删除菜单自身数据
		int rows = sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录已经不存在");
		//5.返回结果
		return rows;
	}

	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int saveObject(SysMenu sysMenu) {
		Assert.notNull(sysMenu,"保存对象不能为空");
		Assert.isEmpty(sysMenu.getName(), "菜单名不能为空");
		int rows = sysMenuDao.insertObject(sysMenu);
		if(rows==0)
			throw new ServiceException("添加失败");
		return rows;
	}

	@Override
	public List<Node> findZtreeMenuNodes() {
		List<Node> list = sysMenuDao.findZtreeMenuNodes();
		return list;
	}

	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int updateObject(SysMenu sysMenu) {
		Assert.notNull(sysMenu,"保存对象不能为空");
		Assert.isEmpty(sysMenu.getName(), "菜单名不能为空");
		int rows = sysMenuDao.updateObject(sysMenu);
		if(rows==0)
			throw new ServiceException("修改失败");
		return rows;
	}

}
