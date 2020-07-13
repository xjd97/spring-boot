package com.cy.pj.sys.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 借助此类封装用户对应的菜单信息
 */
@Data
public class SysUserMenuVo implements Serializable{
	private static final long serialVersionUID = -6532648335943768546L;
	private Integer id;
	private String name;
	private String url;
	private List<SysUserMenuVo> childMenus;
}
