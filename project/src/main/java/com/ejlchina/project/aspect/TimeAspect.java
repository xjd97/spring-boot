package com.ejlchina.project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeAspect {

    @Pointcut("@annotation(com.ejlchina.project.annotation.RequestTime)")
    public void doPointCut() {
    }

    @Around("doPointCut()")
    public Object around(ProceedingJoinPoint pj) throws Throwable {
        long t1 = System.currentTimeMillis();
        Object obj = pj.proceed();
        long t2 = System.currentTimeMillis();
        MethodSignature ms = (MethodSignature) pj.getSignature();
        String name = ms.getName();
        return obj;
    }

}
