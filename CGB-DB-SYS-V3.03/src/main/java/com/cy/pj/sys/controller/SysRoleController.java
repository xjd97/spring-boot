package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@RestController
@RequestMapping("/role/")
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String name,Integer pageCurrent) {
		PageObject<SysRole> records = sysRoleService.findPageObjects(name, pageCurrent);
		return new JsonResult(records);
	}
	
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysRoleService.deleteObject(id);
		return new JsonResult("删除成功");
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysRole sysRole,Integer[] menuIds) {
		sysRoleService.saveObject(sysRole, menuIds);
		return new JsonResult("添加成功");
	}
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		SysRoleMenuVo result = sysRoleService.findObjectById(id);
		return new JsonResult(result);
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysRole sysRole,Integer[] menuIds) {
		sysRoleService.updateObject(sysRole, menuIds);
		return new JsonResult("修改成功");
	}
	
	@RequestMapping("doFindRoles")
	public JsonResult doFindRoles() {
		List<CheckBox> result = sysRoleService.findObjects();
		return new JsonResult(result);
	}
}
