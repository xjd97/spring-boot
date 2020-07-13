package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private PageProperties pageProperties;

	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		Assert.isValid(pageCurrent!=null&&pageCurrent>0, "页码值无效");
		int rowCount = sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("没有找到对应的记录");
		int pageSize = pageProperties.getPageSize();
		int startIndex = (pageCurrent-1)*pageSize;
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		return new PageObject<>(records, rowCount, pageCurrent, pageSize);
	}

	@Override
	public int deleteObject(Integer id) {
		Assert.isValid(id!=null&&id>0,"请先选择");
		sysRoleMenuDao.deleteById("sys_role_menus","role_id",id);
		sysUserRoleDao.deleteById("sys_user_roles","role_id",id);
		int rows = sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
		return rows;
	}

	@Override
	public int saveObject(SysRole sysRole, Integer[] menuIds) {
		Assert.notNull(sysRole, "保存对象不能为空");
		Assert.isEmpty(sysRole.getName(), "角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色分配权限");
		sysRole.setCreatedUser(ShiroUtil.getUsername());
		int rows = sysRoleDao.insertObject(sysRole);
		sysRoleMenuDao.insertObjects(sysRole.getId(), menuIds);
		return rows;
	}

	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		if(id==null||id<=0)
			throw new ServiceException("id值不合法");
		SysRoleMenuVo result = sysRoleDao.findObjectById(id);
		if(result==null)
			throw new ServiceException("记录不存在");
		return result;
	}

	@Override
	public int updateObject(SysRole sysRole, Integer[] menuIds) {
		Assert.notNull(sysRole, "更新的对象不能为空");
		Assert.notNull(sysRole.getId(), "id的值不能为空");
		Assert.isEmpty(sysRole.getName(), "角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色指定至少一个权限");
		int rows = sysRoleDao.updateObject(sysRole);
		if(rows==0)
			throw new ServiceException("对象可能已经不存在");
		sysRoleMenuDao.deleteById("sys_role_menus","role_id",sysRole.getId());
		sysRoleMenuDao.insertObjects(sysRole.getId(), menuIds);
		return rows;
	}

	@Override
	public List<CheckBox> findObjects() {
		List<CheckBox> list = sysRoleDao.findObjects();
		return list;
	}

}
