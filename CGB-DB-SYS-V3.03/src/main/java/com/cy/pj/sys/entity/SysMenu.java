package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class SysMenu implements Serializable{
	private static final long serialVersionUID = 6957715733386177156L;
	private Integer id;
	private String name;
	private String url;
	private Integer type=1;
	private Integer sort;
	private String note;
	private Integer parentId;
	private String permission;
	private Date createdTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;
}
