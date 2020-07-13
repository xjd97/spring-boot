package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;

public interface SysMenuService {
	List<Map<String,Object>> findObjects();
	
	int deleteObject(Integer id);
	
	int saveObject(SysMenu sysMenu);
	
	int updateObject(SysMenu sysMenu);
	
	List<Node> findZtreeMenuNodes();
}
