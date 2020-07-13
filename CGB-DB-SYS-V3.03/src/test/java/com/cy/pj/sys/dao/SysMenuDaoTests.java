package com.cy.pj.sys.dao;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SysMenuDaoTests {
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Test
	public void testSysMenuDao1() {
		Instant start = Instant.now();
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		Instant end = Instant.now();
		System.out.println("time:"+Duration.between(start, end).toMillis());
	}
	
	@Test
	public void testSysMenuDao2() {
		int childCount = sysMenuDao.getChildCount(116);
		System.out.println(childCount);
	}
}
