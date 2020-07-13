package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SysRoleDaoTests {
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Test
	public void testFindPageObjects() {
		List<SysRole> list = sysRoleDao.findPageObjects("运维", 0, 3);
		list.forEach((r)->log.info(r.toString()));
	}
	
	@Test
	public void testFindObjectById() {
		SysRoleMenuVo sysRoleMenuVo = sysRoleDao.findObjectById(1);
		log.info(sysRoleMenuVo.toString());
	}
}
