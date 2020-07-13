package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysLog;

@SpringBootTest
public class SysLogDaoTests {
	@Autowired
	private SysLogDao sysLogDao;
	
	@Test
	public void testfindPageObjects() {
		List<SysLog> list = sysLogDao.findPageObjects("admin");
//		for (SysLog sysLog : list) {
//			System.out.println(sysLog);
//		}
		//JDK8新特性 lambda表达式
		list.forEach((a)->System.out.println(a));
	}
	
	@Test
	public void testDeleteObjects() {
		int rows = sysLogDao.deleteObjects(100,200);
		System.out.println(rows);
	}
}
