package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Order(100)
@Aspect
@Component
@Slf4j
public class SysExceptionAspect {
	
	@AfterThrowing(pointcut="bean(*ServiceImpl)",throwing="e")
	public void doHandlerException(JoinPoint jp,Exception e) {
		MethodSignature ms = (MethodSignature) jp.getSignature();
		log.error("{}'exception msg is {}",ms.getName(),e.getMessage());
	}
}
