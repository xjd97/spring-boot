package com.cy.pj.common.util;


public class Assert {
	private Assert() {}
	public static void notNull(Object object,String message) {
		if(object==null) {
			throw new IllegalArgumentException(message);
		}
	}
	public static void isEmpty(String object,String message) {
		if("".equals(object.trim())||object==null) {
			throw new IllegalArgumentException(message);
		}
	}
	public static void isValid(boolean valid,String message) {
		if(!valid) {
			throw new IllegalArgumentException(message);
		}
	}
}
