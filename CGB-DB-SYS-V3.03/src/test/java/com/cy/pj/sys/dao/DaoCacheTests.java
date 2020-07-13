package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysLog;

@SpringBootTest
public class DaoCacheTests {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@SuppressWarnings("unused")
	@Test
	public void testFirstCache() {//默认一级缓存为Sqlsession对象私有,多个Sqlsesssion之间不会共享一级缓存
		//1.创建Session对象
		SqlSession session = sqlSessionFactory.openSession();
		//2.访问数据库
		List<Map<String, Object>> records01 = session.selectList("com.cy.pj.sys.dao.SysMenuDao.findObjects");
		List<Map<String, Object>> records02 = session.selectList("com.cy.pj.sys.dao.SysMenuDao.findObjects");
		//3.释放资源
		session.close();
	}
	
	/**
	 * 测试mybatis中的二级缓存,可以多个sqlsession对象共享的缓存
	 * 二级缓存对象的key与命名空间有关
	 */
	@Test
	public void testSecondCache01() {
		//1.创建Session对象
		SqlSession session1 = sqlSessionFactory.openSession();
		SqlSession session2 = sqlSessionFactory.openSession();
		//2.访问数据库
		List<Map<String, Object>> records01 = session1.selectList("com.cy.pj.sys.dao.SysMenuDao.findObjects");
		session1.close();//session事务提交或close时才会向二级缓存存数据
		
		List<Map<String, Object>> records02 = session2.selectList("com.cy.pj.sys.dao.SysMenuDao.findObjects");
		System.out.println("records01==records02:"+(records01==records02));
		//3.释放资源
		session2.close();
	}
	
	@Test
	public void testSecondCache02() {
		//1.创建SQLsession对象
		SqlSession session1 = sqlSessionFactory.openSession();
		SqlSession session2 = sqlSessionFactory.openSession();
		//2.查询所有的角色对象
		List<SysLog> list1 = session1.selectList("com.cy.pj.sys.dao.SysLogDao.findPageObjects", "admin");
		session1.close();
		List<SysLog> list2 = session2.selectList("com.cy.pj.sys.dao.SysLogDao.findPageObjects", "admin");
		System.out.println(list1==list2);
		session2.close();
	}
}
