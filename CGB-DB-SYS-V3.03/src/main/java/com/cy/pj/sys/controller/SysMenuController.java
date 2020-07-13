package com.cy.pj.sys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

@RestController
@RequestMapping("/menu/")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping("doFindObjects")
	public JsonResult doFindObjects() {
		List<Map<String, Object>> list = sysMenuService.findObjects();
		return new JsonResult(list);
	}
	
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysMenuService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysMenu sysMenu) {
		sysMenuService.saveObject(sysMenu);
		return new JsonResult("添加成功");
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysMenu sysMenu) {
		sysMenuService.updateObject(sysMenu);
		return new JsonResult("修改成功");
	}
	
	@RequestMapping("doFindZtreeMenuNodes")
	public JsonResult doFindZtreeMenuNodes() {
		List<Node> list = sysMenuService.findZtreeMenuNodes();
		return new JsonResult(list);
	}
}
