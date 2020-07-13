package com.cy.pj.sys.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SysRoleMenuVo implements Serializable{
	private static final long serialVersionUID = -6048591348050051105L;
	private Integer id;
	private String name;
	private String note;
	private List<Integer> menuIds;
}
