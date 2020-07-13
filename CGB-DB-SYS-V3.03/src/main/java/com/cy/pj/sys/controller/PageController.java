package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserMenuVo;

/**
 * 基于此controller呈现页面
 */
@Controller
@RequestMapping("/")
public class PageController {
	@Autowired
	private SysUserService sysUserService;
	//此类型的对象线程安全(底层CAS算法-借助CPU硬件优势)
//	private AtomicLong aLong = new AtomicLong(0);
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
//		String tName = Thread.currentThread().getName();
//		System.out.println("thread.tname="+tName);
//		//记录页面访问次数
//		System.out.println(aLong.incrementAndGet());
		model.addAttribute("username", ShiroUtil.getUsername());
		List<SysUserMenuVo> userMenus = sysUserService.findUserMenusByUserId(ShiroUtil.getLoginUser().getId());
		model.addAttribute("userMenus", userMenus);
		return "starter";
	}
	
//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";//view
//	}
//	
//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
	
	//RESTful(一种软件架构编码风格)的URL映射
	//{}为rest表达式
	//@PathVariable 用于告诉spring mvc参数值从url中获取
	@RequestMapping("{module}/{moduleUI}")
	public String doModuleUI(@PathVariable(name = "moduleUI") String moduleUI) {
		return "sys/"+moduleUI;
	}
	
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	
	@RequestMapping("doLoginUI")
	public String doLoginUI(){
		return "login";
	}
	@RequestMapping("test")
	public String doTest() {
		return "test";
	}

}
