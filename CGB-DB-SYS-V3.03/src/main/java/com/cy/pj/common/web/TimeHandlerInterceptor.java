package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cy.pj.common.exception.ServiceException;
/**
 * Spring MVC中的拦截器(可以在后端控制器执行前后进行请求和响应的拦截处理)
 */
public class TimeHandlerInterceptor implements HandlerInterceptor {
	/**
	 * 此方法在后端控制器方法执行之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//1.获取日历对象
		Calendar c = Calendar.getInstance();
		//2.设置允许访问的开始时间
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long start = c.getTimeInMillis();
		//3.设置终止访问的时间
		c.set(Calendar.HOUR_OF_DAY, 24);
		long end = c.getTimeInMillis();
		long cTime=System.currentTimeMillis();
		if(cTime<start||cTime>end)
			throw new ServiceException("请在规定时间访问:0~24");
		return true;//代表请求是否拦截
	}
	//控制器方法执行之后,视图解析之前
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	//视图解析之后
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
