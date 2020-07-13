package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.vo.SysUserDeptVo;

@SpringBootTest
public class SysUserDaoTests {
	@Autowired
	private SysUserDao sysUserDao;
	
	@Test
	public void testSysUserDao() {
		List<SysUserDeptVo> findObjects = sysUserDao.findPageObjects(null);
		for (SysUserDeptVo sysUserDeptVo : findObjects) {
			System.out.println(sysUserDeptVo.toString());
		}
	}
	
	@Test
	public void testIsExist() {
		int rows = sysUserDao.isExist("sys_users","email", "t@t.com");
		System.out.println("rows="+rows);
	}
}
