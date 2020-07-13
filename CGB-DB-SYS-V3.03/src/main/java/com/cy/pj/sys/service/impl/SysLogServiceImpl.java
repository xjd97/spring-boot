package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

/**
 * 日志模块业务层对象
 *	1)核心业务(数据的基本操作)
 *	2)拓展业务(权限控制,缓存,异步)
 */
@Service
public class SysLogServiceImpl implements SysLogService {
	@Resource
	private SysLogDao sysLogDao;
	@Resource
	private PageProperties pageProperties;

	@Override
	public PageObject<SysLog> findPageObjects(
			String username,
			Integer pageCurrent) {
		//1.参数校验
		//PageUtil.isValid(pageCurrent);
		Assert.isTrue(pageCurrent!=null&&pageCurrent>=1, "页码值无效");
		//2.设置查询起始位置并启动拦截器
		int pageSize = pageProperties.getPageSize();
		Page<SysLog> page = PageHelper.startPage(pageCurrent, pageSize);
		//3.查询当前页日志记录
		List<SysLog> records = sysLogDao.findPageObjects(username);
		//4.封装结果并返回
		return new PageObject<>(records,(int)page.getTotal(),pageCurrent,pageSize);
	}

	@Transactional
	@Override
	public int deleteObjects(Integer...ids) {
		//1.参数校验
		Assert.isTrue(ids!=null&&ids.length>0, "请先选择");
		//2.执行删除业务
		int rows = sysLogDao.deleteObjects(ids);
		if(rows==0) {
			throw new ServiceException("记录可能已经不存在");
		}
		return rows;
	}
	
	/**
	 * @Async 描述的方法,表示让这个方法运行在一个spring框架提供的线程对象中
	 * 这个线程对象来自于一个线程池
	 */
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(SysLog sysLog) {
		sysLogDao.insertObject(sysLog);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
}
